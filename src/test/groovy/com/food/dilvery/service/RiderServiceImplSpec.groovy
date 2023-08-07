package com.food.dilvery.service

import com.food.delivery.common.BeanUtil
import com.food.delivery.domain.*
import com.food.delivery.factory.RiderFactory
import com.food.delivery.model.AvailableRider
import com.food.delivery.repository.*
import com.food.delivery.service.impl.RiderServiceImpl
import org.springframework.context.ApplicationContext
import spock.lang.Specification

import java.time.LocalDateTime

class RiderServiceImplSpec extends Specification
{

    def orderHeaderRepository = Mock(OrderHeaderRepository)
    def customerRepository = Mock(CustomerRepository)
    def restaurantRepository = Mock(RestaurantRepository)
    def foodRepository = Mock(FoodRepository)
    def riderLocationRepository = Mock(RiderLocationRepository)
    def userAccountRepository = Mock(UserAccountRepository)
    def orderTrackingRepository = Mock(OrderTrackingRepository)
    def riderRatingRepository = Mock(RiderRatingRepository)

    def applicationContext = Mock(ApplicationContext)
    def beanUtil = new BeanUtil()

    def riderFactory = new RiderFactory(orderHeaderRepository, userAccountRepository, customerRepository, restaurantRepository);
    def riderServiceImpl = new RiderServiceImpl(riderLocationRepository, userAccountRepository, orderTrackingRepository, riderRatingRepository, riderFactory)

    def setup()
    {
        beanUtil.setApplicationContext(applicationContext)
        foodRepository.findByKeyIn(_) >> [new Food(webId: 545, name: "Chicken")]

    }

    def newOrderHeader()
    {
        return new OrderHeader(webId: 1,
                customer: newCustomer(),
                restaurant: newRestaurant(), orderNumber:"WP-123", priority:1, shipToAddress: newAddress(), expectedDeliveryTime: LocalDateTime.now(), expectedCookingTime: LocalDateTime.now())
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




    def "Scenario 1: add rider under order tracking to monitor the order status"()
    {
        given:

        userAccountRepository.findById(_) >> Optional.of(new UserAccount(webId: 3l, firstName: "Waqar"))
        riderLocationRepository.findNearestRider(_, _) >> [new AvailableRider( 6, 2.0 )]
        orderTrackingRepository.save(_) >>newOrderTracking()
        riderRatingRepository.findAllByRider(_)>>[new RiderRating(webId: 1, rating: 1.0)]

        when:
        riderServiceImpl.addOrderTracking(newOrderHeader())

        then:
        1*orderTrackingRepository.save(_) >>newOrderTracking()
        1*riderLocationRepository.findNearestRider(_, _) >> [new AvailableRider( 6, 2.0 )]
        1*userAccountRepository.findById(_) >> Optional.of(new UserAccount(webId: 3l, firstName: "Waqar"))
    }


    def "Scenario 2: No r rider available to pick the order"()
    {
        given:

        userAccountRepository.findById(_) >> Optional.of(new UserAccount(webId: 3l, firstName: "Waqar"))
        riderLocationRepository.findNearestRider(_, _) >> []
        orderTrackingRepository.save(_) >>newOrderTracking()
        riderRatingRepository.findAllByRider(_)>>[new RiderRating(webId: 1, rating: 1.0)]

        when:
        riderServiceImpl.addOrderTracking(newOrderHeader())

        then:
        0*orderTrackingRepository.save(_) >>newOrderTracking()
        1*riderLocationRepository.findNearestRider(_, _) >> []
        0*userAccountRepository.findById(_) >> Optional.of(new UserAccount(webId: 3l, firstName: "Waqar"))
    }

    def "Scenario 2: get rider rating"()
    {
        given:

        userAccountRepository.findById(_) >> Optional.of(new UserAccount(webId: 3l, firstName: "Waqar"))
        riderLocationRepository.findNearestRider(_, _) >> []
        orderTrackingRepository.save(_) >>newOrderTracking()
        riderRatingRepository.findAllByRider(_)>>[new RiderRating(webId: 1, rating: 1.0)]

        when:
        riderServiceImpl.ridingRating(new UserAccount(webId: 1, firstName: "Waqar", lastName: "khan"))

        then:
        1*riderRatingRepository.findAllByRider(_) >> [new RiderRating(webId: 4, rating: 1.0)]

    }


}
