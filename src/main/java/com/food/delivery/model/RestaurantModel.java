package com.food.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.food.delivery.domain.Restaurant;

import java.time.LocalDateTime;

public record RestaurantModel(@JsonIgnore Long webId, String key, LocalDateTime updateDate,
                              LocalDateTime createdDate, Restaurant.RestaurantStatus restaurantStatus, String name,
                              String  description, String icon, String logo, String phoneNumber, AddressModel addressModel,
                              UserAccountModel owner) { }
