package com.food.delivery.repository;

import com.food.delivery.domain.FoodDelivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodDeliveryRepositoryCustom {
    Page<FoodDelivery> fetchReceivedDeliveryWithPageable(Pageable pageable);

    Page<Long> fetchReceivedDeliveryIdsWithPageable(Pageable pageable);
}
