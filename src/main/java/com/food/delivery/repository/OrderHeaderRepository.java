package com.food.delivery.repository;

import com.food.delivery.domain.OrderHeader;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHeaderRepository extends JpaKeyRepository<OrderHeader, Long>, QuerydslPredicateExecutor<OrderHeader> {
}
