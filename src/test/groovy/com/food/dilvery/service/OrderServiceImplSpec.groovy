package com.food.dilvery.service

import com.food.delivery.common.BeanUtil
import com.food.delivery.common.OrderHelper
import com.food.delivery.domain.*
import com.food.delivery.factory.AddressFactory
import com.food.delivery.factory.OrderFactory
import com.food.delivery.factory.RiderFactory
import com.food.delivery.mappers.OrderHeaderMapStructMapper
import com.food.delivery.model.AddressModel
import com.food.delivery.model.AvailableRider
import com.food.delivery.model.OrderModel
import com.food.delivery.model.OrderTicketModel
import com.food.delivery.repository.*
import com.food.delivery.service.impl.OrderServiceImpl
import com.food.delivery.service.impl.RiderServiceImpl
import org.springframework.context.ApplicationContext
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

import java.time.LocalDateTime

class OrderServiceImplSpec extends Specification
{

    def orderHeaderRepository = Mock(OrderHeaderRepository)
    def orderHeaderMapStructMapper = Mock(OrderHeaderMapStructMapper)
    def customerAddressRepository = Mock(CustomerAddressRepository)
    def restaurantFoodRepository = Mock(RestaurantFoodRepository)
    def customerRepository = Mock(CustomerRepository)
    def restaurantRepository = Mock(RestaurantRepository)
    def sequenceNumberRepository = Mock(SequenceNumberRepository)
    def foodRepository = Mock(FoodRepository)
    def addressRepository = Mock(AddressRepository)
    def riderLocationRepository = Mock(RiderLocationRepository)
    def userAccountRepository = Mock(UserAccountRepository)
    def orderTrackingRepository = Mock(OrderTrackingRepository)
    def riderRatingRepository = Mock(RiderRatingRepository)
    def foodDeliveryRepository = Mock(FoodDeliveryRepository)

    def applicationContext = Mock(ApplicationContext)
    def beanUtil = new BeanUtil()

    OrderHelper orderHelper = new OrderHelper(sequenceNumberRepository);
    def addressFactory = new AddressFactory()
    def orderFactory = new OrderFactory(orderHelper, customerRepository, restaurantRepository, foodRepository, restaurantFoodRepository, addressRepository, addressFactory)
    def riderFactory = new RiderFactory(orderHeaderRepository, userAccountRepository, customerRepository, restaurantRepository);
    def riderServiceImpl = new RiderServiceImpl(riderLocationRepository, userAccountRepository, orderTrackingRepository, riderRatingRepository, riderFactory)
    def orderServiceImpl = new OrderServiceImpl(orderHeaderRepository,  orderHeaderMapStructMapper, customerAddressRepository, restaurantFoodRepository, riderServiceImpl, orderHelper, orderFactory, foodDeliveryRepository)

    def setup()
    {
        orderHelper.ratingThreshold=5
        orderHelper.riderAvgSpeed=25
        beanUtil.setApplicationContext(applicationContext)
        restaurantRepository.findByKey(_) >> Optional.of(newRestaurant())
        foodRepository.findByKeyIn(_) >> [new Food(webId: 545, name: "Chicken")]
        addressRepository.findByKey(_) >> Optional.of(newAddress())
        sequenceNumberRepository.saveAndFlush(_) >> new SequenceNumber(webId: 1, name: "Order")
        restaurantFoodRepository.findAllRestaurantFoodByRestaurantAndFoodIn(_,_) >> [ new RestaurantFood(estimatedCookingTime: 2.0, rating: 5, discountRate: 1, price: 500)]
        restaurantFoodRepository.findTopByRestaurantAndFoodInOrderByEstimatedCookingTimeDesc(_,_) >> new RestaurantFood(estimatedCookingTime: 2.0, rating: 5, discountRate: 1, price: 500)

    }

    def newOrderHeader()
    {
        return new OrderHeader(webId: 1,
                customer: newCustomer(),
                restaurant: newRestaurant(), orderNumber:"WP-123", priority:1, shipToAddress: newAddress(), expectedDeliveryTime: LocalDateTime.now(), expectedCookingTime: LocalDateTime.now())
    }

    def buildOrderHeaderEntitiesWithPage()
    {
        def entities = [newOrderHeader(),newOrderHeader(),newOrderHeader()]

        return new PageImpl<OrderHeader>(entities, PageRequest.of(0, 10), entities.size())
    }


    def newSegment()
    {
        return new OrderSegment(webId: 411, food: new Food(webId: 545, name: "Chicken" ))
    }
    def newCustomer()
    {
        return new Customer(webId: 2, key: "abc", type: Customer.CustomerType.VIP, firstName: "Chris", lastName: "Mike")
    }
    def newRestaurant()
    {
        return new Restaurant(webId: 3,key: "def", status:  Restaurant.RestaurantStatus.ACTIVE, address : newAddress())
    }
    def newAddress()
    {
        return new Address(webId: 4, key:  "jkl", city: "abc", country: "UAE", addressType: Address.AddressType.OFFICE, longitude: 10.0, latitude: 1.0  )
    }

    def newOrderTracking()
    {
        return new OrderTracking(webId: 4, key:  "jkl", distance: 1.0)
    }




    def "Scenario 1: add new food order by customer,"()
    {
        given:
        AddressModel addressMode = new AddressModel( 2,   "", LocalDateTime.now(),LocalDateTime.now(), "City", "country","123", "","s", "s2", Address.AddressType.OFFICE, null, 1.0, 2.0)

        customerAddressRepository.existsByCustomerAndAddress(_) >> false
        userAccountRepository.findById(_) >> Optional.of(new UserAccount(webId: 3l, firstName: "Waqar"))
        customerRepository.findByKey(_) >> Optional.of(newCustomer())
        orderHeaderRepository.save (_) >> newOrderHeader()
        riderLocationRepository.findNearestRider(_, _) >> [new AvailableRider( 6, 2.0 )]
        addressRepository.save(_) >> newAddress()
        orderTrackingRepository.save(_) >>newOrderTracking()
        riderRatingRepository.findAllByRider(_)>>[new RiderRating(webId: 1, rating: 1.0)]

        OrderModel orderModel = new OrderModel(customerKey: "abc", restaurantKey: "def" , shipAddress: addressMode, foods: ["ASD"])

        when:
        orderServiceImpl.create(orderModel)

        then:
        2*orderHeaderRepository.save(_)
        1*orderTrackingRepository.save(_) >>newOrderTracking()
        1*customerAddressRepository.existsByCustomerAndAddress(_, _)
        2*restaurantFoodRepository.findTopByRestaurantAndFoodInOrderByEstimatedCookingTimeDesc(_,_) >> new RestaurantFood(estimatedCookingTime: 2.0, rating: 5, discountRate: 1, price: 500)
        1*riderLocationRepository.findNearestRider(_, _) >> [new AvailableRider( 6, 2.0 )]
        1*riderRatingRepository.findAllByRider(_)>>[new RiderRating(webId: 1, rating: 1.0)]
        1*customerRepository.findByKey(_) >> Optional.of(newCustomer())
        1*addressRepository.save(_) >>newAddress()
        1*userAccountRepository.findById(_) >> Optional.of(new UserAccount(webId: 3l, firstName: "Waqar"))
    }


    def "Scenario 2: If VIP customer created order,It should be marked as high priority order "()
    {


        given:

        customerAddressRepository.existsByCustomerAndAddress(_) >> false
        userAccountRepository.findById(_) >> Optional.of(new UserAccount(webId: 3l, firstName: "Waqar"))
        customerRepository.findByKey(_) >> Optional.of(newCustomer())
        orderHeaderRepository.save (_) >> newOrderHeader()
        riderLocationRepository.findNearestRider(_, _) >> [new AvailableRider( 6, 2.0 )]
        addressRepository.save(_) >> newAddress()
        orderTrackingRepository.save(_) >>newOrderTracking()
        riderRatingRepository.findAllByRider(_)>>[new RiderRating(webId: 1, rating: 1.0)]
        AddressModel addressMode = new AddressModel( 2,   "", LocalDateTime.now(),LocalDateTime.now(), "City", "country","123", "","s", "s2", Address.AddressType.OFFICE, null, 1.0, 2.0)
        OrderModel orderModel = new OrderModel(customerKey: "abc", restaurantKey: "def" , shipAddress: addressMode, foods: ["ASD"])
        when:
        OrderTicketModel response = orderServiceImpl.create(orderModel)

        then:
        response.getPriority() == 1
    }


    def "fetch customer orders in the pageable content"()
    {
        given:

        def orderHeaderEntity = buildOrderHeaderEntitiesWithPage();
        orderHeaderRepository.findAll(_,_) >>  orderHeaderEntity


        when:
        def response = orderServiceImpl.fetchAll("",  1, OrderHeader.OrderHeaderStatus.RECEIVED,null,null, PageRequest.of(0, 10))

        then:
        response.totalElements==3
    }

    def "fetch should call to retrieve the data single object"()
    {
        when:
        orderServiceImpl.fetch("a220c87f-aa53-4099-b549-d0ae973c22a6")

        then:
        callCount * orderHeaderRepository.findByKey("a220c87f-aa53-4099-b549-d0ae973c22a6") >> orderResult

        where:
        callCount | orderResult
        1         | Optional.of(newOrderHeader())
    }

    def "fetch should throw exception as this no record exists against the key"()
    {
        given:
        orderHeaderRepository.findByKey("a220c87f-aa53-4099-b549-d0ae973c22a6") >>Optional.empty()

        when:
        orderServiceImpl.fetch("abc")

        then:
        thrown(Exception)
    }



}
