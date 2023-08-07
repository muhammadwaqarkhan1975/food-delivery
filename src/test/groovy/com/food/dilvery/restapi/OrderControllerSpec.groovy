package com.food.dilvery.restapi


import com.fasterxml.jackson.databind.ObjectMapper
import com.food.delivery.domain.*
import com.food.delivery.model.*
import com.food.delivery.restapi.OrderController
import com.food.delivery.service.OrderService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.LocalDateTime

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class OrderControllerSpec extends Specification
{
    def orderService = Mock(OrderService)
    def orderController = new OrderController(orderService)
    def mapper = new ObjectMapper()


    def mockMvc = MockMvcBuilders.standaloneSetup(orderController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build()


    def newOrder()
    {
        new OrderHeaderModel(1, "abc", LocalDateTime.now(),
                LocalDateTime.now(), newCustomer(), newRestaurant(), "",
                OrderHeader.OrderHeaderStatus.RECEIVED , 1,
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(),
                LocalDateTime.now(),LocalDateTime.now(),
                LocalDateTime.now(),
                newAddress(), new ArrayList<OrderSegmentModel>())
    }
    def newCustomer()
    {
        return new CustomerModel(1, "abc", LocalDateTime.now(),
                LocalDateTime.now(),"Waqar", "khan",
                Customer.CustomerStatus.ACTIVE, Customer.CustomerType.VIP, "123", "")
    }
    def newRestaurant()
    {
        return new RestaurantModel(2, "", LocalDateTime.now(),
                LocalDateTime.now(), Restaurant.RestaurantStatus.ACTIVE , "",
                "", "", "", "", newAddress(),newUserAccount())
    }
    def newUserAccount()
    {
        return new UserAccountModel(1, "", LocalDateTime.now(),
                LocalDateTime.now(),"waqar@khan.com", "Waqar", "khan",
                UserAccount.UserAccountStatus.ACTIVE, "", new ArrayList<UserAddressModel>())
    }

    def newAddress()
    {
        return new AddressModel( 2,   "", LocalDateTime.now(),LocalDateTime.now(), "City", "country","123", "","s", "s2", Address.AddressType.OFFICE, null, 1.0, 2.0)
    }


    def "GET: /api/order/{key} should return the single order against the unique key"() {

        when: "Get all order raised in delivery system"
        def response = mockMvc.perform(
                get("/api/order/{key}", "ABC")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

        then: "status should be 200"
        response.andExpect(status().isOk())
        1 * orderService.fetch(_) >> newOrder()
    }

    def "GET: /api/order/{key} should throw the resource not found exception"() {


        when: "Get all raised in delivery system"
        def response = mockMvc.perform(
                get("/api/order/{key}")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

        then: "Throw IllegalArgumentException when genres not provided"
        thrown IllegalArgumentException.class
    }


    def "GET: /api/order should return the customer orders"()
    {
        when: "Get all customer order raised in delivery system"
        def response = mockMvc.perform(
                get("/api/order")
                        .queryParam("page", "0")
                        .queryParam("size", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

        then: "status should be 200"
        response.andExpect(status().isOk())
        1 * orderService.fetchAll(_,_,_,_,_,_) >> new PageImpl<OrderTicket>([], PageRequest.of(0, 1), 0)
    }

    def "GET: /api/order/{key}/status should return the status if provided order"()
    {
        when: "Get all customer order raised in delivery system"
        def response = mockMvc.perform(
                get("/api/order/{key}/status", "abc")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

        then: "status should be 200"
        response.andExpect(status().isOk())
        1 * orderService.fetch(_) >> newOrder()
    }

    def "Patch: /api/order/{key}/status/{status} should should create order"()
    {
        given:


        when: "patch call is invoked"
        def response = mockMvc.perform(patch("/api/order/{key}/status/{status}", "abc", OrderHeader.OrderHeaderStatus.RECEIVED))

        then:
        noExceptionThrown()
        response.andExpect(status().isOk())
        1*orderService.changeStatus(_, _)
    }

    def "PUT: /api/order/{key}/status/{status} should should create order"()
    {
        given:


        when: "patch call is invoked"
        def response = mockMvc.perform(put("/api/order/{key}/status/{status}", "abc", OrderHeader.OrderHeaderStatus.RECEIVED))

        then:
        noExceptionThrown()
        response.andExpect(status().isOk())
        1*orderService.changeStatus(_, _)
    }


    def "POST: /api/order should should create order"()
    {
        given:
        def inputModel = new FoodDeliveryModel(1, "abc", "WP09", "VIP", FoodDelivery.DeliveryStatus.RECEIVED,
                1, null, 0.0,
                1.0, 0.0, LocalDateTime.now())

        when: "post call is invoked"
        def response = mockMvc.perform(post("/api/order")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(inputModel)))

        then:
        noExceptionThrown()
        response.andExpect(status().isOk())
        orderService.create(_, _, _,_,_) >> inputModel
    }

    def "PUT: /api/order should should create order"()
    {
        given:
        def inputModel = new FoodDeliveryModel(1, "abc", "WP09", "VIP", FoodDelivery.DeliveryStatus.RECEIVED,
                1, null, 0.0,
                1.0, 0.0, LocalDateTime.now())

        when: "post call is invoked"
        def response = mockMvc.perform(post("/api/order")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(inputModel)))

        then:
        noExceptionThrown()
        response.andExpect(status().isOk())
        orderService.create(_, _, _,_,_) >> inputModel
    }
}
