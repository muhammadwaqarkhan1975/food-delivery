package com.food.dilvery.service


import com.food.delivery.domain.FoodDelivery
import com.food.delivery.mappers.FoodDeliveryMapStructMapper
import com.food.delivery.model.FoodDeliveryModel
import com.food.delivery.repository.FoodDeliveryRepository
import com.food.delivery.service.impl.FoodDeliveryServiceImpl
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

import java.time.LocalDateTime

class FoodDeliveryServiceImplSpec extends Specification
{

    def foodDeliveryRepository = Mock(FoodDeliveryRepository)
    def foodDeliveryMapStructMapper = Mock(FoodDeliveryMapStructMapper)

    def foodDeliveryServiceImpl = new FoodDeliveryServiceImpl(foodDeliveryRepository, foodDeliveryMapStructMapper);

    def newFoodDeliverModel()
    {
        return new FoodDeliveryModel(1l, "abc-def","WD-1", "VIP", FoodDelivery.DeliveryStatus.RECEIVED,
                1, LocalDateTime.now(),0.0,0.0, 0.0, LocalDateTime.now())
    }

    def newFoodDeliver()
    {
        return new FoodDelivery(webId: 1, customer: "VIP")

    }

    def buildFoodDelivery()
    {
        def entities = [new FoodDelivery(webId: 1, customer: "VIP"),
                        new FoodDelivery(webId: 1, customer: "LOYAL"),new FoodDelivery(webId: 1, customer: "NEW")
        ]

        return new PageImpl<FoodDelivery>(entities, PageRequest.of(0, 10), entities.size())
    }
    def buildFoodDeliveryList()
    {
        return [new FoodDelivery(webId: 1, customer: "VIP"),
                        new FoodDelivery(webId: 1, customer: "LOYAL"),new FoodDelivery(webId: 1, customer: "NEW")
        ]

    }

    def "Scenario 1: post food delivery statistics"()
    {
        given:
        def deliveryModel = newFoodDeliverModel()

        foodDeliveryMapStructMapper.entity(_) >> new FoodDelivery(webId: 1, customer: "VIP")
        foodDeliveryMapStructMapper.model(_) >>  newFoodDeliverModel()
        when:
        foodDeliveryServiceImpl.create(deliveryModel)

        then:
        1*foodDeliveryRepository.save(_)  >> new FoodDelivery(webId: 1, customer: "VIP")
        1*foodDeliveryMapStructMapper.entity(_) >>new FoodDelivery(webId: 1, customer: "VIP")
        1*foodDeliveryMapStructMapper.model(_) >>  newFoodDeliverModel()


    }


    def "fetchAll should call to retrieve the data as pageable object"()
    {

        when:
        foodDeliveryServiceImpl.fetchAll("", "", "VIP",FoodDelivery.DeliveryStatus.RECEIVED, 1 , PageRequest.of(0, 10))

        then:
        callCount * foodDeliveryRepository.findAll(_,_) >> result

        where:
        callCount | result
        1         | buildFoodDelivery()
    }


    def "fetch should call to retrieve the data single object"()
    {
        when:
        foodDeliveryServiceImpl.fetch("a220c87f-aa53-4099-b549-d0ae973c22a6")

        then:
        callCount * foodDeliveryRepository.findByKey("a220c87f-aa53-4099-b549-d0ae973c22a6") >> result

        where:
        callCount | result
        1         | Optional.of(newFoodDeliver())
    }

    def "fetch should throw exception as abc key does not exists in context"()
    {
        given:
        foodDeliveryRepository.findByKey("a220c87f-aa53-4099-b549-d0ae973c22a6") >>Optional.empty()

        when:
        foodDeliveryServiceImpl.fetch("abc")

        then:
        thrown(Exception)
    }

    def "Retrieve the ticket on the base of priority"()
    {
        when:
        foodDeliveryServiceImpl.fetchPriorityTicket()

        then:
        callCount * foodDeliveryRepository.findAllByOrderByPriorityAsc() >> result

        where:
        callCount | result
        1         | buildFoodDeliveryList()
    }



}
