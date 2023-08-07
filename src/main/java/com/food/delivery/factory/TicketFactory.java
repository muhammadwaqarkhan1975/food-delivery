package com.food.delivery.factory;

import com.food.delivery.common.BeanUtil;
import com.food.delivery.common.OrderHelper;
import com.food.delivery.domain.FoodDelivery;
import com.food.delivery.domain.Ticket;

public class TicketFactory
{

    private static final TicketFactory FACTORY	= new TicketFactory();
    private final OrderHelper orderHelper;
    /**
     * Returns an instance of the factory
     */
    public static TicketFactory getInstance ()
    {
        return FACTORY;
    }

    private TicketFactory()
    {
        orderHelper = BeanUtil.getBean(OrderHelper.class);
    }

    public Ticket newTicket (FoodDelivery foodDelivery, Ticket.TicketReason reason)
    {
        Ticket ticket = new Ticket();
        ticket.setFoodDelivery(foodDelivery);
        ticket.setReason(reason.getReason());
        ticket.setStatus(Ticket.TicketStatus.ACTIVE);
        return  ticket;
    }


}
