package com.food.delivery.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_tracking")
@Getter
@Setter
public class OrderTracking extends BaseEntity
{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderHeader orderHeader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id")
    private UserAccount userAccount;

    @Column(name = "distance")
    private double distance;

    @Column(name = "current_latitude")
    private double latitude;

    @Column(name = "current_longitude")
    private double longitude;



}
