package com.food.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.food.delivery.domain.OrderHeader;

import java.time.LocalDateTime;
import java.util.List;
public record OrderHeaderModel(@JsonIgnore Long webId, String key, LocalDateTime updateDate,
                               LocalDateTime createdDate, CustomerModel customerModel, RestaurantModel restaurantModel, String orderNumber,
                               OrderHeader.OrderHeaderStatus status, Integer priority,
                               LocalDateTime expectedCookingTime,LocalDateTime actualCookingTime,LocalDateTime expectedPickupTime,
                               LocalDateTime actualPickupTime,LocalDateTime expectedDeliveryTime,
                               LocalDateTime actualDeliveryTime,
                               AddressModel shipToAddress, List<OrderSegmentModel> orderSegments) { }
