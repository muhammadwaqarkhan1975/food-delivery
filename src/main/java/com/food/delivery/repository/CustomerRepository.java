package com.food.delivery.repository;

import com.food.delivery.domain.Customer;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaKeyRepository<Customer, Long>, QuerydslPredicateExecutor<Customer> {
}
