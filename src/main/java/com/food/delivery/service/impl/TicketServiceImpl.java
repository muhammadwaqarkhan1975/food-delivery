package com.food.delivery.service.impl;

import com.food.delivery.domain.FoodDelivery;
import com.food.delivery.domain.QViewTicket;
import com.food.delivery.domain.Ticket;
import com.food.delivery.exception.ResourceNotFoundException;
import com.food.delivery.model.TicketModel;
import com.food.delivery.repository.ViewTicketRepository;
import com.food.delivery.service.TicketService;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final ViewTicketRepository viewTicketRepository;
    @Override
    public Page<TicketModel> fetchAll(String search, String deliverId, String customer, FoodDelivery.DeliveryStatus deliveryStatus,
                                       Integer priority, Double riderRating, Ticket.TicketStatus ticketStatus, Pageable pageable)
    {
        BooleanBuilder filter = new BooleanBuilder();
        if (StringUtils.hasText(search))
        {
            filter.and(QViewTicket.viewTicket.deliveryId.containsIgnoreCase(search));
            filter.and(QViewTicket.viewTicket.customer.containsIgnoreCase(search));
        }

        if (StringUtils.hasText(deliverId))
            filter.and(QViewTicket.viewTicket.deliveryId.containsIgnoreCase(deliverId));

        if (StringUtils.hasText(customer))
            filter.and(QViewTicket.viewTicket.customer.containsIgnoreCase(customer));

        if (!ObjectUtils.isEmpty(deliveryStatus))
            filter.and(QViewTicket.viewTicket.deliveryStatus.eq(deliveryStatus));

        if (!ObjectUtils.isEmpty(priority))
            filter.and(QViewTicket.viewTicket.priority.eq(priority));

        if (!ObjectUtils.isEmpty(ticketStatus))
            filter.and(QViewTicket.viewTicket.ticketStatus.eq(ticketStatus));

        return viewTicketRepository.findAll(filter,pageable).map(TicketModel::new);

    }

    @Override
    public TicketModel fetch(String key) {
        return new TicketModel(viewTicketRepository.findByKey(key).orElseThrow(() -> new ResourceNotFoundException(Ticket.class.getSimpleName(), key , HttpStatus.NOT_FOUND)));
    }

    @Override
    public List<TicketModel> fetchPriorityTicket() {
        return viewTicketRepository.findAllByOrderByPriorityAsc().stream().map(TicketModel::new).collect(Collectors.toList());
    }
}
