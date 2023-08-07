package com.food.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.food.delivery.domain.RiderRating;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class RiderRatingListModel
{
    @JsonIgnore
    private Long webId;
    private String key;
    private LocalDateTime updateDate;
    private LocalDateTime createdDate;
    private String rider;
    private String riderKey;
    private String customer;
    private String customerKey;
    private String order;
    private String orderKey;
    private String  restaurant;
    private String  restaurantKey;
    private double  rating;
    private String  comment;

    public RiderRatingListModel(RiderRating riderRating)
    {
        this.webId = riderRating.getWebId();
        this.key = riderRating.getKey();
        this.updateDate = riderRating.getUpdateDate();
        this.createdDate = riderRating.getCreatedDate();
        this.rider = riderRating.getRider().getFirstName() + " " + riderRating.getRider().getLastName();
        this.riderKey = riderRating.getRider().getKey();
        this.order = riderRating.getOrderHeader().getOrderNumber();
        this.orderKey = riderRating.getOrderHeader().getKey();
        this.restaurant = riderRating.getRestaurant().getName();
        this.restaurantKey = riderRating.getRestaurant().getKey();
        this.customer = riderRating.getRestaurant().getKey();
        this.customerKey = riderRating.getRestaurant().getKey();
        this.rating = riderRating.getRating();
        this.comment = riderRating.getComment();
     }

}
