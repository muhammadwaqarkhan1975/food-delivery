package com.food.delivery.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.food.delivery.domain.Customer;
import com.food.delivery.model.CustomerModel;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService
{
    CustomerModel create(CustomerModel customerModel);

    CustomerModel update(String  key, CustomerModel customerModel);

    CustomerModel patch(String  key, JsonPatch customerModel) throws JsonPatchException, JsonProcessingException;

    Page<CustomerModel> fetchAll(String search, Customer.CustomerStatus customerStatus, Customer.CustomerType type, Pageable pageable);

    CustomerModel fetch(String  key);


}
