package com.food.delivery.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.delivery.repository.FoodDeliveryRepository;
import com.food.delivery.service.MonitoringExecutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(value="spring.food.delivery.monitoring.consumer", havingValue = "true")
public class SqsConsumer
{
    private final MonitoringExecutorService monitoringExecutorService;
    private final FoodDeliveryRepository foodDeliveryRepository;

    @SqsListener(value = "${food.delivery.monitoring.queue}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void notification(String message)
    {
        try
        {
            List<Integer> deliveriesIds = new ObjectMapper().readValue(message, List.class);

            monitoringExecutorService.execute(foodDeliveryRepository.findAllById(deliveriesIds.stream().map(Integer::longValue).collect(Collectors.toList())));
        }
        catch (Throwable ex)
        {
            //In case of exception, fallback strategy to publish the event into the kafka topic to handle the failure messages
            log.error(ex.getMessage());
        }
    }
}
