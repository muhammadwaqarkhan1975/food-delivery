package com.food.delivery.repository;

import com.food.delivery.domain.Address;
import com.food.delivery.domain.Customer;
import com.food.delivery.domain.CustomerAddress;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAddressRepository extends JpaKeyRepository<CustomerAddress, Long>, QuerydslPredicateExecutor<CustomerAddress> {

    boolean existsByCustomerAndAddress(Customer customer, Address address);
}
