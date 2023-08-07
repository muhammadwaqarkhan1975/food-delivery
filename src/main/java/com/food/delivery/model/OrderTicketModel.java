package com.food.delivery.model;


import com.food.delivery.domain.Customer;
import com.food.delivery.domain.OrderHeader;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderTicketModel
{
    private String orderKey;
    private String orderNumber;
    private Customer.CustomerType customerType;
    private OrderHeader.OrderHeaderStatus  orderStatus ;
    private LocalDateTime expectedTime;
    private Double distance;
    private Double riderRating;
    private Double foodMeanTime;
    private LocalDateTime timeToReachDestination;
    private Integer priority;

    public OrderTicketModel(OrderHeader orderHeader, Double riderRating, Double distance, Double restaurantMeanTime)
    {
        this.orderNumber = orderHeader.getOrderNumber();
        this.orderKey = orderHeader.getKey();
        this.distance = !ObjectUtils.isEmpty(distance) ? new BigDecimal(distance).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0.0;
        this.customerType = orderHeader.getCustomer().getType();
        this.orderStatus= orderHeader.getStatus();
        this.expectedTime = orderHeader.getExpectedDeliveryTime();
        this.riderRating = riderRating;
        this.foodMeanTime = !ObjectUtils.isEmpty(restaurantMeanTime) ? new BigDecimal(restaurantMeanTime).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0.0;;
        this.timeToReachDestination = orderHeader.getActualDeliveryTime();
        this.priority = orderHeader.getPriority();
    }


}
