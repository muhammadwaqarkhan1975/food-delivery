package com.food.delivery.repository;

import com.food.delivery.domain.Address;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaKeyRepository<Address, Long>, QuerydslPredicateExecutor<Address> {
}
