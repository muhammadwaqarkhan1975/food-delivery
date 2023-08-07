package com.food.delivery.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_header")
@Getter
@Setter
public class OrderHeader extends BaseEntity
{
    public enum OrderHeaderStatus {RECEIVED,ORDER_PREPARING,ORDER_PICKUP,IN_ROUTE,DELIVERED,INVOICED;}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "expected_cooking_time")
    private LocalDateTime expectedCookingTime;

    @Column(name = "actual_cooking_time")
    private LocalDateTime actualCookingTime;

    @Column(name = "expected_pickup_time")
    private LocalDateTime expectedPickupTime;

    @Column(name = "actual_pickup_time")
    private LocalDateTime actualPickupTime;

    @Column(name = "expected_delivery_time")
    private LocalDateTime expectedDeliveryTime;

    @Column(name = "actual_delivery_time")
    private LocalDateTime actualDeliveryTime;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private OrderHeaderStatus status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_to_address")
    private Address shipToAddress;

    @OneToMany(mappedBy = "orderHeader", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderSegment> orderSegments = new ArrayList<>();

    @OneToOne(mappedBy = "orderHeader",fetch = FetchType.LAZY)
    private OrderTracking orderTracking;


    public void setStatus(OrderHeaderStatus status)
    {
         switch (status) {
            case ORDER_PICKUP   -> {
                setActualPickupTime(LocalDateTime.now());
                setActualCookingTime(LocalDateTime.now());
            }
             case IN_ROUTE   -> {
                 if (this.actualPickupTime == null) {
                     setActualPickupTime(LocalDateTime.now());
                 }
             }
            case DELIVERED   -> setActualDeliveryTime(LocalDateTime.now());
        }
        this.status = status;
    }
}
