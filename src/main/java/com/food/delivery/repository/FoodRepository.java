package com.food.delivery.repository;

import com.food.delivery.domain.Food;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaKeyRepository<Food, Long>, QuerydslPredicateExecutor<Food>
{
    List<Food> findByKeyIn(List<String> keys);
}
