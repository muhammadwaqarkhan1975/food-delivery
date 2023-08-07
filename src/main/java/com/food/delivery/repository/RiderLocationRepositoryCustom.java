package com.food.delivery.repository;

import com.food.delivery.model.AvailableRider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiderLocationRepositoryCustom {
    List<AvailableRider> findNearestRider(Double latitude, Double longitude);
}
