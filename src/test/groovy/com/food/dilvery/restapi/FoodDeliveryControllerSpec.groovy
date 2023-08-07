package com.food.dilvery.restapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.food.delivery.domain.FoodDelivery
import com.food.delivery.model.FoodDeliveryModel
import com.food.delivery.restapi.FoodDeliveryController
import com.food.delivery.service.FoodDeliveryService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.LocalDateTime

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class FoodDeliveryControllerSpec extends Specification
{
    def foodDeliveryService = Mock(FoodDeliveryService)
    def foodDeliveryController = new FoodDeliveryController(foodDeliveryService)
    def mapper = new ObjectMapper()


    def mockMvc = MockMvcBuilders.standaloneSetup(foodDeliveryController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build()

    def "GET: /api/foodDelivery/{key} should return the single food delivery against the unique key"() {

        when: "Get all tickets raised in delivery system"
        def response = mockMvc.perform(
                get("/api/foodDelivery/{key}", "ABC")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

        then: "status should be 200"
        response.andExpect(status().isOk())
        1 * foodDeliveryService.fetch(_) >>  new FoodDeliveryModel(1, "abc", "WP09", "VIP", FoodDelivery.DeliveryStatus.RECEIVED,
                1, LocalDateTime.now(), 0.0,
                1.0, 0.0, LocalDateTime.now())
    }

    def "GET: /api/foodDelivery/{key} should throw the resource not found exception"() {


        when: "Get all raised in delivery system"
        def response = mockMvc.perform(
                get("/api/foodDelivery/{key}")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

        then: "Throw IllegalArgumentException when genres not provided"
        thrown IllegalArgumentException.class
    }


    def "GET: /api/foodDelivery should return the food deliveries"() {


        when: "Get all food deliveries raised in delivery system"
        def response = mockMvc.perform(
                get("/api/foodDelivery")
                        .queryParam("page", "0")
                        .queryParam("size", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

        then: "status should be 200"
        response.andExpect(status().isOk())
        1 * foodDeliveryService.fetchAll(_,_,_,_,_,_) >> new PageImpl<FoodDeliveryModel>([], PageRequest.of(0, 1), 0)
    }

    def "GET: /api/foodDelivery/priority should return the tickets on the base or priority asc"() {


        when: "Get all tickets raised in delivery system"
        def response = mockMvc.perform(
                get("/api/foodDelivery/priority")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

        then: "status should be 200"
        response.andExpect(status().isOk())
        1 * foodDeliveryService.fetchPriorityTicket() >>  []
    }

    def "POST: /api/foodDelivery should create food delivery"()
    {
        given:
        def inputModel = new FoodDeliveryModel(1, "abc", "WP09", "VIP", FoodDelivery.DeliveryStatus.RECEIVED,
                1, null, 0.0,
                1.0, 0.0, LocalDateTime.now())

        when: "post call is invoked"
        def response = mockMvc.perform(post("/api/foodDelivery")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(inputModel)))

        then:
        noExceptionThrown()
        response.andExpect(status().isOk())
        foodDeliveryService.create(_, _, _,_,_) >> inputModel


    }
}
