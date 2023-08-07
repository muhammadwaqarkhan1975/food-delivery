package com.food.delivery.schedule;

import com.food.delivery.domain.FoodDelivery;
import com.food.delivery.service.MonitoringExecutorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@Scope("prototype")
@AllArgsConstructor
public class FoodDeliveryWorker implements Runnable
{
    private final List<FoodDelivery> foodDeliveries;
    private final MonitoringExecutorService monitoringExecutorService;


    /*
        FoodDeliveryWorker will be executed parallel to perform the calculation
    * */
    @Override
    public void run()
    {
        monitoringExecutorService.execute(foodDeliveries);
    }
}
