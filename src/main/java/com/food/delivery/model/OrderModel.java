package com.food.delivery.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class OrderModel
{
    private String customerKey;
    private String restaurantKey;
    private AddressModel shipAddress;
    private List<String> foods = new ArrayList<>();
}
