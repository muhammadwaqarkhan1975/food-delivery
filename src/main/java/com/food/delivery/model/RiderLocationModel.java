package com.food.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
public record RiderLocationModel(@JsonIgnore Long webId, String key,
                                 @JsonSerialize(using = LocalDateTimeSerializer.class)
                                 @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                                 LocalDateTime updateDate,
                                 @JsonSerialize(using = LocalDateTimeSerializer.class)
                                 @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                                 LocalDateTime createdDate, String riderKey,  double latitude, double longitude) { }
