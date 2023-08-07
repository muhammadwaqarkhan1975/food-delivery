package com.food.delivery.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_segment")
@Getter
@Setter
public class OrderSegment extends BaseEntity
{
    public enum OrderSegmentStatus {ACTIVE,INACTIVE;}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderHeader orderHeader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private OrderSegmentStatus status;

    @Column(name = "discount_rate")
    private Double discountRate;

    @Column(name = "price")
    private Double price;

}
