package com.food.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.food.delivery.domain.OrderSegment;

import java.time.LocalDateTime;
public record OrderSegmentModel(@JsonIgnore Long webId, String key, LocalDateTime updateDate,
                                LocalDateTime createdDate, OrderSegment.OrderSegmentStatus orderSegmentStatus,
                                OrderHeaderModel orderHeaderModel, FoodModel foodModel,
                                Double discount, Double price) { }