package com.food.delivery.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "restaurant_food")
@Getter
@Setter
public class RestaurantFood extends BaseEntity
{
    public enum RestaurantFoodStatus {ACTIVE,INACTIVE;}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "estimated_cooking_time")
    private Double estimatedCookingTime;

    @Column(name = "discount_rate")
    private Double discountRate;

    @Column(name = "price")
    private Double price;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private RestaurantFoodStatus status;

    @Column(name = "image")
    private String image;
}
