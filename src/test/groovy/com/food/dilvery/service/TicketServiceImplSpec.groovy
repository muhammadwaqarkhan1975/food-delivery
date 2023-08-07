package com.food.dilvery.service

import com.food.delivery.domain.FoodDelivery
import com.food.delivery.domain.Ticket
import com.food.delivery.domain.ViewTicket
import com.food.delivery.repository.ViewTicketRepository
import com.food.delivery.service.impl.TicketServiceImpl
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

class TicketServiceImplSpec extends Specification
{
    def viewTicketRepository = Mock(ViewTicketRepository)
    def ticketServiceImpl = new TicketServiceImpl(viewTicketRepository)

    def buildTicketEntitiesWithPage()
    {
        def entities = [new ViewTicket(webId: 12, priority: 1, key: "a220c87f-aa53-4099-b549-d0ae973c22a6",  deliveryId: "WR-09", customer: "VIP", deliveryStatus:  "RECEIVED",
                distance: 1, riderRating: 1, foodMeanTime: 10, ticketStatus: Ticket.TicketStatus.ACTIVE),
                         new ViewTicket(webId: 13, priority: 2, key: "a220c87f-aa53-4099-b549-d0ae973c22a6",  deliveryId: "WR-09", customer: "LOYAL", deliveryStatus:  "RECEIVED",
                                 distance: 1, riderRating: 1, foodMeanTime: 10, ticketStatus: Ticket.TicketStatus.ACTIVE),
                         new ViewTicket(webId: 13, priority: 3, key: "a220c87f-aa53-4099-b549-d0ae973c22a6",  deliveryId: "WR-09", customer: "NEW", deliveryStatus:  "RECEIVED",
                                 distance: 1, riderRating: 1, foodMeanTime: 10, ticketStatus: Ticket.TicketStatus.ACTIVE)
        ]

        return new PageImpl<ViewTicket>(entities, PageRequest.of(0, 10), entities.size())
    }

    def buildTicketEntities()
    {
        return [new ViewTicket(webId: 12, priority: 1, key: "a220c87f-aa53-4099-b549-d0ae973c22a6",  deliveryId: "WR-09", customer: "VIP", deliveryStatus:  "RECEIVED",
                distance: 1, riderRating: 1, foodMeanTime: 10, ticketStatus: Ticket.TicketStatus.ACTIVE),
                            new ViewTicket(webId: 13, priority: 2, key: "a220c87f-aa53-4099-b549-d0ae973c22a6",  deliveryId: "WR-09", customer: "LOYAL", deliveryStatus:  "RECEIVED",
                                    distance: 1, riderRating: 1, foodMeanTime: 10, ticketStatus: Ticket.TicketStatus.ACTIVE),
                            new ViewTicket(webId: 13, priority: 3, key: "a220c87f-aa53-4099-b549-d0ae973c22a6",  deliveryId: "WR-09", customer: "NEW", deliveryStatus:  "RECEIVED",
                                    distance: 1, riderRating: 1, foodMeanTime: 10, ticketStatus: Ticket.TicketStatus.ACTIVE)
        ]

    }

    def buildTicketEntity()
    {
        return Optional.of(new ViewTicket(webId: 12, priority: 1, key: "a220c87f-aa53-4099-b549-d0ae973c22a6",  deliveryId: "WR-09", customer: "VIP", deliveryStatus:  "RECEIVED",
                distance: 1, riderRating: 1, foodMeanTime: 10, ticketStatus: Ticket.TicketStatus.ACTIVE))
    }

    def "ticket service should return the pageable content"()
    {
        given:

       def ticketEntity = buildTicketEntitiesWithPage();
        viewTicketRepository.findAll(_,_) >>  ticketEntity


        when:
        def response = ticketServiceImpl.fetchAll("", "", "", FoodDelivery.DeliveryStatus.RECEIVED,
                0, 0.0, Ticket.TicketStatus.ACTIVE , PageRequest.of(0, 10))

        then:
        response.totalElements==3
    }



    def "fetchAll should call to retrieve the data as pageable object"()
    {

        when:
        ticketServiceImpl.fetchAll("", "", "", FoodDelivery.DeliveryStatus.RECEIVED,
                0, 0.0, Ticket.TicketStatus.ACTIVE , PageRequest.of(0, 10))

        then:
        callCount * viewTicketRepository.findAll(_,_) >> ticketResult

        where:
        callCount | ticketResult
        1         | buildTicketEntitiesWithPage()
    }


    def "fetch should call to retrieve the data single object"()
    {
        when:
        ticketServiceImpl.fetch("a220c87f-aa53-4099-b549-d0ae973c22a6")

        then:
        callCount * viewTicketRepository.findByKey("a220c87f-aa53-4099-b549-d0ae973c22a6") >> ticketResult

        where:
        callCount | ticketResult
        1         | buildTicketEntity()
    }

    def "fetch should throw exception as abc key does not exists in context"()
    {
        given:
        viewTicketRepository.findByKey("a220c87f-aa53-4099-b549-d0ae973c22a6") >>Optional.empty()

        when:
        ticketServiceImpl.fetch("abc")

        then:
        thrown(Exception)
    }

    def "Retrieve the ticket on the base of priority"()
    {
        when:
        ticketServiceImpl.fetchPriorityTicket()

        then:
        callCount * viewTicketRepository.findAllByOrderByPriorityAsc() >> ticketResult

        where:
        callCount | ticketResult
        1         | buildTicketEntities()
    }





}
