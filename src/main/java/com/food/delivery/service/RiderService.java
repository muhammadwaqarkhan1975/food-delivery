package com.food.delivery.service;

import com.food.delivery.domain.OrderHeader;
import com.food.delivery.domain.OrderTracking;
import com.food.delivery.domain.UserAccount;

public interface RiderService
{
   OrderTracking addOrderTracking(OrderHeader orderHeader);

    Double ridingRating(UserAccount userAccount);
}
