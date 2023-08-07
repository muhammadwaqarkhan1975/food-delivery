package com.food.delivery.service;

import com.food.delivery.model.RiderLocationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RiderLocationService
{
    RiderLocationModel create(RiderLocationModel riderLocationModel);

    RiderLocationModel update(String  key, RiderLocationModel riderLocationModel);


    Page<RiderLocationModel> fetchAll(String search, String riderKey, Pageable pageable);

    RiderLocationModel fetch(String  key);
}
