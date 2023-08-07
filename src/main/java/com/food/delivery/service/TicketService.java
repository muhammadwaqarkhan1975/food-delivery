package com.food.delivery.service;

import com.food.delivery.domain.FoodDelivery;
import com.food.delivery.domain.Ticket;
import com.food.delivery.model.TicketModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketService
{
    Page<TicketModel> fetchAll(String search, String deliverId, String customer, FoodDelivery.DeliveryStatus deliveryStatus,
                               Integer priority, Double riderRating, Ticket.TicketStatus ticketStatus, Pageable pageable);

    TicketModel fetch(String  key);
    List<TicketModel> fetchPriorityTicket();
}
