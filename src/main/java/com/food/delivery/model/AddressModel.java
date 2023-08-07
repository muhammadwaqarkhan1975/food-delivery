package com.food.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.food.delivery.domain.Address;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public record AddressModel(@JsonIgnore Long webId, String key, LocalDateTime updateDate,
                           LocalDateTime createdDate, String city, String country, String postalCode,
                           String state, String street, String street2, Address.AddressType addressType, String county,
                           Double latitude, Double longitude) {
}
