package com.food.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.food.delivery.domain.FoodDelivery;
import com.food.delivery.domain.Ticket;
import com.food.delivery.domain.ViewTicket;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TicketModel
{
    @JsonIgnore
    private Long webId;
    private String key;
    private String deliveryId;
    private String customer;
    private FoodDelivery.DeliveryStatus deliveryStatus;
    private Integer priority;
    private LocalDateTime expectedDeliveryTime;
    private Double distance;
    private Double riderRating;
    private Double foodMeanTime;
    private LocalDateTime actualDeliveryTime;
    private String reason;
    private Ticket.TicketStatus ticketStatus;

    public TicketModel(ViewTicket viewTicket)
    {
        this.webId = viewTicket.getWebId();
        this.key = viewTicket.getKey();
        this.deliveryId = viewTicket.getDeliveryId();
        this.customer = viewTicket.getCustomer();
        this.deliveryStatus = viewTicket.getDeliveryStatus();
        this.priority = viewTicket.getPriority();
        this.expectedDeliveryTime = viewTicket.getExpectedDeliveryTime();
        this.distance = viewTicket.getDistance();
        this.riderRating = viewTicket.getRiderRating();
        this.foodMeanTime = viewTicket.getFoodMeanTime();
        this.actualDeliveryTime = viewTicket.getActualDeliveryTime();
        this.reason = viewTicket.getReason();
        this.ticketStatus = viewTicket.getTicketStatus();
        this.reason = viewTicket.getReason();

    }


}
