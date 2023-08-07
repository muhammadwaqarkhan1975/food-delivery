package com.food.delivery.service.impl;

import com.food.delivery.common.OrderHelper;
import com.food.delivery.domain.FoodDelivery;
import com.food.delivery.domain.Ticket;
import com.food.delivery.factory.TicketFactory;
import com.food.delivery.repository.FoodDeliveryRepository;
import com.food.delivery.repository.TicketRepository;
import com.food.delivery.service.MonitoringExecutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class MonitoringExecutorServiceImpl implements MonitoringExecutorService
{
    private final FoodDeliveryRepository foodDeliveryRepository;
    private final TicketRepository ticketRepository;
    private final OrderHelper orderHelper;

    @Override
    @Transactional
    public void execute(List<FoodDelivery> foodDeliveries) {
        foodDeliveries.forEach(foodDelivery -> {
            try
            {
                orderHelper.calculatePriority(foodDelivery);

                if(orderHelper.canDeliverOnTime(foodDelivery))
                {
                    ticketRepository.save(TicketFactory.getInstance()
                            .newTicket(foodDelivery,orderHelper.isVipCustomer(foodDelivery.getCustomer()) ?
                                    Ticket.TicketReason.REASON_DELIVERY_TIME_EXCEED_VIP_CUSTOMER :
                                    Ticket.TicketReason.REASON_DELIVERY_TIME_EXCEED));
                }

                foodDeliveryRepository.save(foodDelivery);
            }
            catch (Exception e)
            {
                //In case of exception, fallback strategy to publish the event into the kafka topic to handle the failure messages
                log.error("schedule, having an issue with {} order, It will be picked in next iteration", foodDelivery.getDeliveryId());
            }
        });
    }
}
