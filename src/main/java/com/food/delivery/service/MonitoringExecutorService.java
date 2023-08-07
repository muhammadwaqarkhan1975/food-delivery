package com.food.delivery.service;

import com.food.delivery.domain.FoodDelivery;

import java.util.List;

public interface MonitoringExecutorService
{
    void execute(List<FoodDelivery> foodDeliveries);
}
