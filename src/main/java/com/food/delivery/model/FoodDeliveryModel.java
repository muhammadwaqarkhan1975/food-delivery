package com.food.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.food.delivery.domain.FoodDelivery;

import java.time.LocalDateTime;

public record FoodDeliveryModel(@JsonIgnore Long webId, String key, String deliveryId, String customer, FoodDelivery.DeliveryStatus deliveryStatus,
                                Integer priority,
                                @JsonSerialize(using = LocalDateTimeSerializer.class)
                                @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                                LocalDateTime expectedDeliveryTime, double distance,
                                double riderRating, double foodMeanTime,
                                @JsonSerialize(using = LocalDateTimeSerializer.class)
                                @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                                LocalDateTime actualDeliveryTime) { }
