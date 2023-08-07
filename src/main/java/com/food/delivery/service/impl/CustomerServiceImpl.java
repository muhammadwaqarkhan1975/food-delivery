package com.food.delivery.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.food.delivery.domain.Customer;
import com.food.delivery.domain.QCustomer;
import com.food.delivery.exception.ResourceNotFoundException;
import com.food.delivery.mappers.CustomerMapStructMapper;
import com.food.delivery.model.CustomerModel;
import com.food.delivery.repository.CustomerRepository;
import com.food.delivery.service.CustomerService;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.querydsl.core.BooleanBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService
{
    private final CustomerRepository customerRepository;
    private final CustomerMapStructMapper customerMapStructMapper;
    @Override
    @Transactional
    public CustomerModel create(CustomerModel customerModel) {
        return customerMapStructMapper.model(customerRepository.save(customerMapStructMapper.entity(customerModel)));
    }

    @Override
    @Transactional
    public CustomerModel update(String  key, CustomerModel customerModel) {

        Customer customer =  customerRepository.findByKey(key)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase()));
        customer.setAvatar(customerModel.avatar());
        customer.setFirstName(customerModel.firstName());
        customer.setLastName(customerModel.lastName());
        customer.setStatus(customerModel.status());
        customer.setType(customerModel.type());
        customer.setPhoneNumber(customerModel.phoneNumber());
        return customerMapStructMapper.model(customerRepository.save(customer));
    }

    @Override
    @Transactional
    public CustomerModel patch(String  key, JsonPatch customerModel) throws JsonPatchException, JsonProcessingException {
        return null;
    }

    @Override
    public Page<CustomerModel> fetchAll(String search, Customer.CustomerStatus customerStatus, Customer.CustomerType type, Pageable pageable) {

        BooleanBuilder filter = new BooleanBuilder();
        if (StringUtils.hasText(search)) {
            filter.and(QCustomer.customer.firstName.containsIgnoreCase(search))
                    .or(QCustomer.customer.lastName.containsIgnoreCase(search))
                    .or(QCustomer.customer.phoneNumber.containsIgnoreCase(search));
        }

        if (!ObjectUtils.isEmpty(customerStatus)) {
            filter.and(QCustomer.customer.status.eq(customerStatus));
        }

        if (!ObjectUtils.isEmpty(type)) {
            filter.and(QCustomer.customer.type.eq(type));
        }

        return customerRepository.findAll(filter,pageable).map(customer -> customerMapStructMapper.model(customer));
    }

    @Override
    public CustomerModel fetch(String key) {

        return customerMapStructMapper.model(customerRepository.findByKey(key)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase()))) ;
    }
}
