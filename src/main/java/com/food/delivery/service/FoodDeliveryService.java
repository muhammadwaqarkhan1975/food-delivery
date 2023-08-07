package com.food.delivery.service;

import com.food.delivery.domain.FoodDelivery;
import com.food.delivery.model.FoodDeliveryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FoodDeliveryService
{
    FoodDeliveryModel create(FoodDeliveryModel foodDeliveryModel);

    Page<FoodDeliveryModel> fetchAll(String search, String deliveryId, String customer, FoodDelivery.DeliveryStatus deliveryStatus, Integer priority, Pageable pageable);
    List<FoodDeliveryModel> fetchPriorityTicket();

    FoodDeliveryModel fetch(String  key);
}
