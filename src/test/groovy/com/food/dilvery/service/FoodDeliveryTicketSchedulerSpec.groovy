package com.food.dilvery.service

import com.food.delivery.common.BeanUtil
import com.food.delivery.common.OrderHelper
import com.food.delivery.domain.FoodDelivery
import com.food.delivery.repository.FoodDeliveryRepository
import com.food.delivery.repository.SequenceNumberRepository
import com.food.delivery.repository.TicketRepository
import com.food.delivery.schedule.FoodDeliveryTicketScheduler
import com.food.delivery.schedule.FoodDeliveryWorker
import com.food.delivery.service.impl.MonitoringExecutorServiceImpl
import org.springframework.context.ApplicationContext
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import spock.lang.Specification

import java.time.LocalDateTime

class FoodDeliveryTicketSchedulerSpec extends Specification
{
    def applicationContext = Mock(ApplicationContext)
    def beanUtil = new BeanUtil()
    def foodDeliveryRepository = Mock(FoodDeliveryRepository)
    def sequenceNumberRepository = Mock(SequenceNumberRepository)
    def ticketRepository = Mock(TicketRepository)
    def threadPoolTaskExecutor = Mock(ThreadPoolTaskExecutor)
    def orderHelper = Mock(OrderHelper)
    def monitoringExecutorService = new MonitoringExecutorServiceImpl(foodDeliveryRepository, ticketRepository, new OrderHelper(sequenceNumberRepository))
    def foodDeliveryTicketScheduler = new FoodDeliveryTicketScheduler(foodDeliveryRepository, threadPoolTaskExecutor, monitoringExecutorService, 1)


    def setup()
    {
        beanUtil.setApplicationContext(applicationContext)
    }
    def newFooDeliveries()
    {
        return new PageImpl<FoodDelivery>([
                new FoodDelivery(webId: 1, key: "6672f876-8727-4596-a89d-beabaef5f6e2", deliveryId: "WR-007",
                        customer: "VIP", deliveryStatus: FoodDelivery.DeliveryStatus.RECEIVED,
                        expectedDeliveryTime:  LocalDateTime.now().plusHours(1) , actualDeliveryTime: LocalDateTime.now().plusHours(2)),

                new FoodDelivery(webId: 2, key: "54dedfe5-4572-4c44-bb66-2ed07651ecc5", deliveryId: "WR-008",
                        customer: "LOYAL", deliveryStatus: FoodDelivery.DeliveryStatus.RECEIVED),

                new FoodDelivery(webId: 3, key: "5547c342-6eed-4bfd-89be-a36187ffa411", deliveryId: "WR-009",
                        customer: "NEW", deliveryStatus: FoodDelivery.DeliveryStatus.RECEIVED)], PageRequest.of(0, 3), 1)
    }

    def vipCustomer()
    {
        def foodDelivery = Mock(FoodDelivery)
        foodDelivery.getCustomer() >> "VIP"
        foodDelivery.getRiderRating() >> 4
        foodDelivery.getExpectedDeliveryTime() >> LocalDateTime.now().plusHours(1)
        foodDelivery.getFoodMeanTime() >> 10
        foodDelivery.getActualDeliveryTime() >> LocalDateTime.now().plusHours(1)
        return foodDelivery;

    }

    def "execute the batch job to update the priority, raised a ticket "()
    {
        given:
        def foodDelivery = newFooDeliveries()

        foodDeliveryRepository.fetchReceivedDeliveryWithPageable(_) >> foodDelivery

        when:
        foodDeliveryTicketScheduler.updateDeliveries()

        then:
        2 * foodDeliveryRepository.fetchReceivedDeliveryWithPageable(_) >> foodDelivery
        1 * threadPoolTaskExecutor.execute(_)
    }

    def "Thread worker: Scenario 1: Calculate priority should invoke, canDeliverOnTime should return false, foodDeliveryRepository.save should store the priority, No ticket expected"()
    {
        given:
        def foodDelivery = vipCustomer()
        def monitoringExecutorService = new MonitoringExecutorServiceImpl(foodDeliveryRepository, ticketRepository, orderHelper)
        def foodDeliveryWorker = new FoodDeliveryWorker( [vipCustomer()], monitoringExecutorService)

        when:
        foodDeliveryWorker.run()

        then:
        1 * orderHelper.calculatePriority(_)
        1 * orderHelper.canDeliverOnTime(_)
        1 * foodDeliveryRepository.save(_) >> foodDelivery
        0 * ticketRepository.save(_)


    }

    def "Thread worker: Scenario 2:  SetPriority should invoke three as per business, Customer is VIP and rider rating is more then 3, No ticket will be created"()
    {
        given:
        def foodDelivery = Mock(FoodDelivery)
        foodDelivery.getCustomer() >> "VIP"
        foodDelivery.getPriority() >> 1
        foodDelivery.getRiderRating() >> 4
        foodDelivery.getExpectedDeliveryTime() >> LocalDateTime.now().plusHours(1)
        foodDelivery.getFoodMeanTime() >> 10
        foodDelivery.getActualDeliveryTime() >> LocalDateTime.now().plusHours(1)

        def foodDeliveryWorker = new FoodDeliveryWorker( [foodDelivery], monitoringExecutorService)

        when:
        foodDeliveryWorker.run()

        then:
        1 * foodDeliveryRepository.save(_)
        2 * foodDelivery.setPriority(_)
        0 * ticketRepository.save(_)
    }


    def "Thread worker: Scenario 3:  setPriority and can deliver on time should invoke, new ticket should create as expected delivery time has already passed"()
    {
        given:
        def foodDelivery = Mock(FoodDelivery)
        foodDelivery.getCustomer() >> "VIP"
        foodDelivery.getRiderRating() >> 4
        foodDelivery.getPriority() >> 0
        foodDelivery.getExpectedDeliveryTime() >> LocalDateTime.now().plusHours(-1)
        foodDelivery.getFoodMeanTime() >> 10
        foodDelivery.getActualDeliveryTime() >> LocalDateTime.now().plusHours(-1)
        def orderHelper = new OrderHelper(sequenceNumberRepository)
        orderHelper.ratingThreshold=5
        def monitoringExecutorService = new MonitoringExecutorServiceImpl(foodDeliveryRepository, ticketRepository, orderHelper)

        def foodDeliveryWorker = new FoodDeliveryWorker( [foodDelivery], monitoringExecutorService)

        when:
        foodDeliveryWorker.run()

        then:
        1 * foodDeliveryRepository.save(_)
        2 * foodDelivery.setPriority(_)
        1 * ticketRepository.save(_)
    }

    def "Thread worker: Scenario 4: setPriority would never invoke again if VIP priority already set, No new ticket"()
    {
        given:
        def foodDelivery = Mock(FoodDelivery)
        foodDelivery.getCustomer() >> "VIP"
        foodDelivery.getRiderRating() >> 4
        foodDelivery.getPriority() >> 1
        foodDelivery.getExpectedDeliveryTime() >> LocalDateTime.now().plusHours(1)
        foodDelivery.getFoodMeanTime() >> 10
        foodDelivery.getActualDeliveryTime() >> LocalDateTime.now().plusHours(1)

        def orderHelper = new OrderHelper(sequenceNumberRepository)
        orderHelper.ratingThreshold=5
        def monitoringExecutorService = new MonitoringExecutorServiceImpl(foodDeliveryRepository, ticketRepository, orderHelper)

        def foodDeliveryWorker = new FoodDeliveryWorker( [foodDelivery], monitoringExecutorService)

        when:
        foodDeliveryWorker.run()

        then:
        1 * foodDeliveryRepository.save(_)
        1 * foodDelivery.setPriority(_)
        0 * ticketRepository.save(_)
    }

    def "Thread worker: Scenario 7: VIP customer priority should set to 1"()
    {
        given:
        def orderHelper = new OrderHelper(sequenceNumberRepository)
        orderHelper.ratingThreshold=4
        def monitoringExecutorService = new MonitoringExecutorServiceImpl(foodDeliveryRepository, ticketRepository, orderHelper)

        when:
        new FoodDeliveryWorker( [foodDelivery], monitoringExecutorService).run()

        then:
        1 * foodDeliveryRepository.save(_) >>{
                    result = it.get(0).getPriority()
                    return it
                }
        result == 1
        where:

        foodDelivery                                                                                                                                                                                                   |   result
        new FoodDelivery(webId: 1, customer: "VIP", riderRating: 3, expectedDeliveryTime:  LocalDateTime.now().plusHours(1), actualDeliveryTime:   LocalDateTime.now().plusHours(1), foodMeanTime: 10)                 |     1
        new FoodDelivery(webId: 1, customer: "VIP", priority: 2, riderRating: 3, expectedDeliveryTime:  LocalDateTime.now().plusHours(1), actualDeliveryTime:   LocalDateTime.now().plusHours(1), foodMeanTime: 10)    |     1
        new FoodDelivery(webId: 1, customer: "VIP", priority: 0, riderRating: 3, expectedDeliveryTime:  LocalDateTime.now().plusHours(1), actualDeliveryTime:   LocalDateTime.now().plusHours(1), foodMeanTime: 10)    |     1

    }


    def "Thread worker: Scenario 8: LOYAL customer priority should set to 2"()
    {
        given:
        def orderHelper = new OrderHelper(sequenceNumberRepository)
        orderHelper.ratingThreshold=4
        def monitoringExecutorService = new MonitoringExecutorServiceImpl(foodDeliveryRepository, ticketRepository, orderHelper)

        when:
        new FoodDeliveryWorker( [foodDelivery], monitoringExecutorService).run()

        then:
        1 * foodDeliveryRepository.save(_) >>{
            result = it.get(0).getPriority()
            return it
        }
        result == 2
        where:

        foodDelivery                                                                                                                                                                                                     |   result
        new FoodDelivery(webId: 1, customer: "LOYAL", riderRating: 3, expectedDeliveryTime:  LocalDateTime.now().plusHours(1), actualDeliveryTime:   LocalDateTime.now().plusHours(1), foodMeanTime: 10)                 |     2
        new FoodDelivery(webId: 1, customer: "LOYAL", priority: 0, riderRating: 3, expectedDeliveryTime:  LocalDateTime.now().plusHours(1), actualDeliveryTime:   LocalDateTime.now().plusHours(1), foodMeanTime: 10)    |     2
        new FoodDelivery(webId: 1, customer: "LOYAL", priority: 1, riderRating: 3, expectedDeliveryTime:  LocalDateTime.now().plusHours(1), actualDeliveryTime:   LocalDateTime.now().plusHours(1), foodMeanTime: 10)    |     2

    }

    def "Thread worker: Scenario 9: NEW customer priority should set to 3"()
    {
        given:
        def orderHelper = new OrderHelper(sequenceNumberRepository)
        orderHelper.ratingThreshold=4
        def monitoringExecutorService = new MonitoringExecutorServiceImpl(foodDeliveryRepository, ticketRepository, orderHelper)

        when:
        new FoodDeliveryWorker( [foodDelivery], monitoringExecutorService).run()

        then:
        1 * foodDeliveryRepository.save(_) >>{
            result = it.get(0).getPriority()
            return it
        }
        result == 3
        where:

        foodDelivery                                                                                                                                                                                                   |   result
        new FoodDelivery(webId: 1, customer: "NEW", riderRating: 3, expectedDeliveryTime:  LocalDateTime.now().plusHours(1), actualDeliveryTime:   LocalDateTime.now().plusHours(1), foodMeanTime: 10)                 |     3
        new FoodDelivery(webId: 1, customer: "NEW", priority: 0, riderRating: 3, expectedDeliveryTime:  LocalDateTime.now().plusHours(1), actualDeliveryTime:   LocalDateTime.now().plusHours(1), foodMeanTime: 10)    |     3
        new FoodDelivery(webId: 1, customer: "NEW", priority: 1, riderRating: 3, expectedDeliveryTime:  LocalDateTime.now().plusHours(1), actualDeliveryTime:   LocalDateTime.now().plusHours(1), foodMeanTime: 10)    |     3

    }
}
