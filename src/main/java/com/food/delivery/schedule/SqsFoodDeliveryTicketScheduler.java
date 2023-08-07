package com.food.delivery.schedule;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.delivery.repository.FoodDeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.food.delivery.common.OrderHelper.toPriorityPageable;

@RequiredArgsConstructor
@Slf4j
public class SqsFoodDeliveryTicketScheduler implements FoodDeliverySchedule
{
    private final FoodDeliveryRepository foodDeliveryRepository;
    private final AmazonSQSAsync amazonSQSAsync;
    private final int batchPageSize;
    private final String queueName;
    private String queueUrl;


    /**
     * If we deployed this microservice on multiple ec2, We can turn ON/OFF through spring.food.delivery.priority.run.job property.
     * Purpose of this method is to perform the job to calculate the below tasks
     *        1. If the Customer is VIP then customer should have high priority over other customers
     *        2. If the Expected time of delivery is passed and the order is still not delivered, Its priority automatically becomes higher than others
     *        3. Calculate the estimated time of delivery by considering
     * Implementation
     *              Sqs will be handy to divide the processing, To enable the SQS we have to turn ON some properties file
     *                  spring.food.delivery.priority.run.job=sqs
     *                  food.delivery.monitoring.queue=dev-food-delivery
     *                  cloud.aws.credentials.access-key
     *                  cloud.aws.credentials.secret-key
     * In case of huge records in existing table, I use Kafka CDC connector to process those records
     */

    @Scheduled(cron = "${spring.food.delivery.priority.cron:0 */1 * * * *}")
    @Transactional
    public void updateDeliveries() throws JsonProcessingException {
        Pageable pageableRequest = toPriorityPageable (0, batchPageSize);

        Page<Long> foodDeliveryPage = foodDeliveryRepository.fetchReceivedDeliveryIdsWithPageable(pageableRequest);

        int size =0;
        while (size < foodDeliveryPage.getTotalPages())
        {
            publishSqs(foodDeliveryPage.getContent());
            size++;
            pageableRequest = toPriorityPageable(size, batchPageSize);
            foodDeliveryPage = foodDeliveryRepository.fetchReceivedDeliveryIdsWithPageable(pageableRequest);
        }
    }

    /*
    * Purpose of this method is to divide the load, It could be a possibility that at certain time load may increase.
    * So to process the records in parallel way, We have to publish the message in SQS so that number of EC2 instance/machine can consume those messages
    * As long as load increase, We can increase the count of ec2 instance to divide the load
    * I am sending the deliveries ID's in sqs message because of minimum payload should transfer over the network
    * */
    private void publishSqs(List<Long> message) throws JsonProcessingException {
        String messageBody = new ObjectMapper().writeValueAsString(message);
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(getQueueUrl())
                .withMessageBody(messageBody);
        amazonSQSAsync.sendMessage(sendMessageRequest);
    }

    private String getQueueUrl()
    {
        if(StringUtils.isNotBlank(queueUrl))
            return queueUrl;
        else
            queueUrl = amazonSQSAsync.getQueueUrl(queueName).getQueueUrl();
        return queueUrl;
    }
}
