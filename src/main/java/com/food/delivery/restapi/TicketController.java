package com.food.delivery.restapi;


import com.food.delivery.domain.FoodDelivery;
import com.food.delivery.domain.Ticket;
import com.food.delivery.model.TicketModel;
import com.food.delivery.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ticket")
public class TicketController
{
    private final TicketService ticketService;
    @GetMapping
    public ResponseEntity<Page<TicketModel>> getTicket(@RequestParam(required = false) String search,
                                                       @RequestParam(required = false) String deliverId,
                                                       @RequestParam(required = false) String customer,
                                                       @RequestParam(required = false) FoodDelivery.DeliveryStatus deliveryStatus,
                                                       @RequestParam(required = false) Integer priority,
                                                       @RequestParam(required = false) Double riderRating,
                                                       @RequestParam(required = false) Ticket.TicketStatus ticketStatus,
                                                       Pageable pageable)
    {
        return ResponseEntity.ok(ticketService.fetchAll(search, deliverId, customer,deliveryStatus,
                priority, riderRating, ticketStatus,  pageable));
    }

    @GetMapping(value = "/priority")
    public ResponseEntity<List<TicketModel>> getTicket()
    {
        return ResponseEntity.ok(ticketService.fetchPriorityTicket());
    }

    @GetMapping("/{key}")
    public ResponseEntity<TicketModel> getTicket(@PathVariable String key)
    {
        return ResponseEntity.ok(ticketService.fetch(key));
    }
}
