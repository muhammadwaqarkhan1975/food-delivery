package com.food.delivery.repository;

import com.food.delivery.domain.SequenceNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceNumberRepository extends JpaRepository<SequenceNumber, Long>, QuerydslPredicateExecutor<SequenceNumber> {
}
