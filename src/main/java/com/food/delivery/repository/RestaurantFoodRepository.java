package com.food.delivery.repository;

import com.food.delivery.domain.Food;
import com.food.delivery.domain.Restaurant;
import com.food.delivery.domain.RestaurantFood;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantFoodRepository extends JpaKeyRepository<RestaurantFood, Long>, QuerydslPredicateExecutor<RestaurantFood>
{
    List<RestaurantFood> findAllRestaurantFoodByRestaurantAndFoodIn(Restaurant restaurant, List<Food> foods);
    RestaurantFood findTopByRestaurantAndFoodInOrderByEstimatedCookingTimeDesc(Restaurant restaurant, List<Food> foods);
}
