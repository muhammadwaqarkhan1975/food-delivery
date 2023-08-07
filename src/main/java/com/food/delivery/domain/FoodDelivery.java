package com.food.delivery.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "food_delivery")
@Getter
@Setter
@ToString
public class FoodDelivery
{
    public enum DeliveryStatus {RECEIVED,ORDER_PREPARING,ORDER_PICKUP,IN_ROUTE,DELIVERED,INVOICED;}
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernateSequence")
    @GenericGenerator(name = "hibernateSequence", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator")
    @Column(name = "web_id", unique = true, nullable = false)
    private Long webId;

    @Column(name = "key")
    private String key;

    @Column(name = "delivery_id")
    private String deliveryId;

    @Column(name = "customer")
    private String customer;

    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column(name = "expected_delivery_time")
    private LocalDateTime expectedDeliveryTime;

    @Column(name = "distance")
    private Double distance;

    @Column(name = "rider_rating")
    private Double riderRating;

    @Column(name = "food_mean_time")
    private Double foodMeanTime;

    @Column(name = "actual_delivery_time")
    private LocalDateTime actualDeliveryTime;

    @Column(name = "priority")
    private Integer priority;

    @PrePersist
    public void setKey() {

        this.key = StringUtils.hasText(this.key)? this.key: (this.key = UUID.randomUUID().toString());
    }
}
