package com.food.delivery.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vw_ticket")
@Getter
@Setter
@ToString
public class ViewTicket
{
    @Id
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
    private FoodDelivery.DeliveryStatus deliveryStatus;

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

    @Column(name = "ticket_key")
    private String ticketKey;

    @Column(name = "reason")
    private String reason;

    @Column(name = "ticket_status")
    @Enumerated(EnumType.ORDINAL)
    private Ticket.TicketStatus ticketStatus;

    @Column(name = "updated_date")
    private LocalDateTime updateDate;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
