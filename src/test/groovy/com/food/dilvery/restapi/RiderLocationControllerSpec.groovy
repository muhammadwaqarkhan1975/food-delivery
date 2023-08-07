package com.food.dilvery.restapi


import com.fasterxml.jackson.databind.ObjectMapper
import com.food.delivery.model.RiderLocationModel
import com.food.delivery.restapi.RiderLocationController
import com.food.delivery.service.RiderLocationService
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

class RiderLocationControllerSpec extends Specification
{
    def riderLocationService = Mock(RiderLocationService)
    def riderLocationController = new RiderLocationController(riderLocationService)
    def mapper = new ObjectMapper()


    def mockMvc = MockMvcBuilders.standaloneSetup(riderLocationController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build()

    def "GET: /api/riderLocation/{key} should return the single rider location against the unique key"() {

        when: "Get rider location"
        def response = mockMvc.perform(
                get("/api/riderLocation/{key}", "ABC")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

        then: "status should be 200"
        response.andExpect(status().isOk())
        1 * riderLocationService.fetch(_) >>  new RiderLocationModel(1, "abc", LocalDateTime.now(),
                LocalDateTime.now(),"anc", 0.0, 0.0)
    }

    def "GET: /api/riderLocation/{key} should throw the resource not found exception"() {


        when: "Get all raised in delivery system"
        def response = mockMvc.perform(
                get("/api/riderLocation/{key}")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

        then: "Throw IllegalArgumentException when genres not provided"
        thrown IllegalArgumentException.class
    }


    def "GET: /api/riderLocation should return the rider locations"() {


        when: "Get all rider locations"
        def response = mockMvc.perform(
                get("/api/riderLocation")
                        .queryParam("page", "0")
                        .queryParam("size", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))

        then: "status should be 200"
        response.andExpect(status().isOk())
        1 * riderLocationService.fetchAll(_,_,_) >> new PageImpl<RiderLocationModel>([], PageRequest.of(0, 1), 0)
    }

    def "POST: /api/riderLocation should create food delivery"()
    {
        given:
        def inputModel = new RiderLocationModel(1, "", LocalDateTime.now(), LocalDateTime.now(), "", 0.0, 0.0)

        when: "post call is invoked"
        def response = mockMvc.perform(post("/api/riderLocation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(inputModel)))

        then:
        noExceptionThrown()
        response.andExpect(status().isOk())
        riderLocationService.create(_) >> inputModel
    }
}
