package com.food.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
public record RiderRatingModel(@JsonIgnore Long webId, String key, LocalDateTime updateDate,
                               LocalDateTime createdDate, String riderKey, String customerKey, String orderKey,
                               String restaurantKey, double rating, String comment) { }
