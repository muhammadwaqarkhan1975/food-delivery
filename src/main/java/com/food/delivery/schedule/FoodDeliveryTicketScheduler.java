package com.food.delivery.schedule;

import com.food.delivery.domain.FoodDelivery;
import com.food.delivery.repository.FoodDeliveryRepository;
import com.food.delivery.service.MonitoringExecutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;

import static com.food.delivery.common.OrderHelper.toPriorityPageable;

@RequiredArgsConstructor
public class FoodDeliveryTicketScheduler implements  FoodDeliverySchedule
{
    private final FoodDeliveryRepository foodDeliveryRepository;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final MonitoringExecutorService monitoringExecutorService;
    private final int batchPageSize;


    /**
     * If we deployed this microservice on multiple ec2, We can turn ON/OFF through spring.food.delivery.priority.run.job property.
     * Purpose of this method is to perform the background job to calculate the below tasks
     *        1. If the Customer is VIP then customer should have high priority over other customers
     *        2. If the Expected time of delivery is passed and the order is still not delivered, Its priority automatically becomes higher than others
     *        3. Calculate the estimated time of delivery by considering
     * Implementation
     *              Thread executor will hady to perform the parallel process with configured thread poll size along with queue size
     *              We can increase the below property on the base of load and size of ec2 instance, It will help us to do the parallel processing
     *                      spring.food.delivery.pool.size
     *                      spring.food.delivery.max.pool.size
     *                      spring.food.delivery.queue.capacity
     *
     * In case of huge records in existing table, I use Kafka CDC connector to process those records
     */

    @Scheduled(cron = "${spring.food.delivery.priority.cron:0 */1 * * * *}")
    @Transactional
    public void updateDeliveries()
    {
        Pageable pageableRequest = toPriorityPageable (0, batchPageSize);

        Page<FoodDelivery> foodDeliveryPage = foodDeliveryRepository.fetchReceivedDeliveryWithPageable(pageableRequest);

        int size =0;
        while (size < foodDeliveryPage.getTotalPages())
        {
            threadPoolTaskExecutor.execute(new FoodDeliveryWorker(foodDeliveryPage.getContent(), monitoringExecutorService));

            size++;
            pageableRequest = toPriorityPageable(size, batchPageSize);
            foodDeliveryPage = foodDeliveryRepository.fetchReceivedDeliveryWithPageable(pageableRequest);
        }
    }
}
