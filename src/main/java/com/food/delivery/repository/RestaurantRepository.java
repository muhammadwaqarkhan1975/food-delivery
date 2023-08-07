package com.food.delivery.repository;

import com.food.delivery.domain.Restaurant;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaKeyRepository<Restaurant, Long>, QuerydslPredicateExecutor<Restaurant> {
}
