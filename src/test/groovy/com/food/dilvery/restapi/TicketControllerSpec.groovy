package com.food.dilvery.restapi

import com.food.delivery.domain.ViewTicket
import com.food.delivery.model.TicketModel
import com.food.delivery.restapi.TicketController
import com.food.delivery.service.TicketService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class TicketControllerSpec extends Specification
{
    def ticketService = Mock(TicketService)
    def ticketController = new TicketController(ticketService)

    def mockMvc = MockMvcBuilders.standaloneSetup(ticketController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build()

    def "GET: /api/ticket/{key} should return the single ticket against the unique key"() {

        def viewTicket = Mock(ViewTicket)

        when: "Get all tickets raised in delivery system"
        def response = mockMvc.perform(
                get("/api/ticket/{key}", "ABC")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

        then: "status should be 200"
        response.andExpect(status().isOk())
        1 * ticketService.fetch(_) >>  new TicketModel(viewTicket)
    }

    def "GET: /api/ticket/{key} should throw the resource not found exception"() {


        when: "Get all tickets raised in delivery system"
        def response = mockMvc.perform(
                get("/api/ticket/{key}")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

        then: "Throw IllegalArgumentException when genres not provided"
        thrown IllegalArgumentException.class
    }


    def "GET: /api/ticket should return the tickets"() {

        def viewTicket = Mock(ViewTicket)

        when: "Get all tickets raised in delivery system"
        def response = mockMvc.perform(
                get("/api/ticket")
                        .queryParam("page", "0")
                        .queryParam("size", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

        then: "status should be 200"
        response.andExpect(status().isOk())
        1 * ticketService.fetchAll(_,_,_,_,_,_,_,_) >> new PageImpl<TicketModel>([], PageRequest.of(0, 1), 0)
    }

    def "GET: /api/ticket/priority should return the tickets on the base or priority asc"() {


        when: "Get all tickets raised in delivery system"
        def response = mockMvc.perform(
                get("/api/ticket/priority")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

        then: "status should be 200"
        response.andExpect(status().isOk())
        1 * ticketService.fetchPriorityTicket() >>  []
    }
}
