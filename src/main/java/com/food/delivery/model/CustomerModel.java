package com.food.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.food.delivery.domain.Customer;

import java.time.LocalDateTime;

public record CustomerModel(@JsonIgnore Long webId, String key, LocalDateTime updateDate,
                            LocalDateTime createdDate, String firstName, String lastName,
                            Customer.CustomerStatus status, Customer.CustomerType type, String phoneNumber, String avatar) { }