package com.food.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.food.delivery.domain.Food;

import java.time.LocalDateTime;
public record FoodModel(@JsonIgnore Long webId, String key, LocalDateTime updateDate,
                        LocalDateTime createdDate, String name, String description, String category, Food.FoodStatus status) { }
