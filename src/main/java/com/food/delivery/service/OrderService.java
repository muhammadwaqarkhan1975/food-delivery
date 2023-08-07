package com.food.delivery.service;

import com.food.delivery.domain.OrderHeader;
import com.food.delivery.model.OrderHeaderModel;
import com.food.delivery.model.OrderModel;
import com.food.delivery.model.OrderTicketModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService
{
    OrderTicketModel create(OrderModel orderModel);

    Page<OrderHeaderModel> fetchAll(String search, Integer priority, OrderHeader.OrderHeaderStatus status,
                                    List<String> customerKeys, List<String> restaurantKeys, Pageable pageable);
    OrderHeaderModel fetch(String  key);

    void changeStatus(String  key, OrderHeader.OrderHeaderStatus status);
}
