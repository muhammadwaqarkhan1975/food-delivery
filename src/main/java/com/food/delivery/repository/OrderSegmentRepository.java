package com.food.delivery.repository;

import com.food.delivery.domain.OrderSegment;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderSegmentRepository extends JpaKeyRepository<OrderSegment, Long>, QuerydslPredicateExecutor<OrderSegment> {
}
