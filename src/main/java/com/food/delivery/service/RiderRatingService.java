package com.food.delivery.service;

import com.food.delivery.model.RiderRatingListModel;
import com.food.delivery.model.RiderRatingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RiderRatingService
{
    RiderRatingModel create(RiderRatingModel riderRatingModel);

    RiderRatingModel update(String  key, RiderRatingModel riderRatingModel);


    Page<RiderRatingListModel> fetchAll(String search, String riderKey, String orderKey, String customerKey, String restaurantKey, Pageable pageable);

    RiderRatingListModel fetch(String  key);
}
