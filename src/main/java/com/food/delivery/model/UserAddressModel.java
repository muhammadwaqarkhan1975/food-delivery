package com.food.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
public record UserAddressModel(@JsonIgnore Long webId, String key, LocalDateTime updateDate,
                               LocalDateTime createdDate, UserAccountModel userAccountModel, AddressModel addressModel) { }
