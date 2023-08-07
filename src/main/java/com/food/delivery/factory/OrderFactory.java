package com.food.delivery.factory;

import com.food.delivery.common.OrderHelper;
import com.food.delivery.domain.*;
import com.food.delivery.exception.ResourceNotFoundException;
import com.food.delivery.model.AddressModel;
import com.food.delivery.model.OrderModel;
import com.food.delivery.model.OrderTicketModel;
import com.food.delivery.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderFactory
{
    private final OrderHelper orderHelper;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodRepository foodRepository;
    private final RestaurantFoodRepository restaurantFoodRepository;
    private final AddressRepository addressRepository;
    private final AddressFactory addressFactory;
    /**
     * Returns an instance of the factory
     *
     */


    public FoodDelivery newOrderFoodDelivery(OrderTicketModel orderTicketModel)
    {
        FoodDelivery foodDelivery = new FoodDelivery();
        foodDelivery.setDeliveryId(orderTicketModel.getOrderNumber());
        foodDelivery.setCustomer(orderTicketModel.getCustomerType().name());
        foodDelivery.setDeliveryStatus(FoodDelivery.DeliveryStatus.RECEIVED);
        foodDelivery.setDeliveryStatus(FoodDelivery.DeliveryStatus.RECEIVED);
        foodDelivery.setExpectedDeliveryTime(orderTicketModel.getExpectedTime());
        foodDelivery.setDistance(orderTicketModel.getDistance());
        foodDelivery.setRiderRating(orderTicketModel.getRiderRating());
        foodDelivery.setFoodMeanTime(orderTicketModel.getFoodMeanTime());
        foodDelivery.setActualDeliveryTime(orderTicketModel.getTimeToReachDestination() == null ? orderTicketModel.getExpectedTime() : orderTicketModel.getTimeToReachDestination());
        foodDelivery.setPriority(orderTicketModel.getPriority());
        return foodDelivery;
    }
    /**
     * Factory method for use by application code when constructing a new Order for insertion into persistent store. A new
     * Order number is generated automatically.
     *
     * @param orderModel
     *            The customer id to use for the order.
     *            The restaurant id is used to identify the order restaurant
     *            The ship address is to deliver the order
     *            The food requested by customer to deliver
     */
    public OrderHeader newOrder (OrderModel orderModel)
    {
        Customer customer     =  customerRepository.findByKey(orderModel.getCustomerKey()).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase(), Customer.class.getName() + orderModel.getCustomerKey()));
        Restaurant restaurant =  restaurantRepository.findByKey(orderModel.getRestaurantKey()).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase()));
        List<Food> foods      =  foodRepository.findByKeyIn(orderModel.getFoods());

        return newOrder (customer, restaurant , shipAddress(orderModel.getShipAddress()), foods);
    }


    /**
     * Factory method for use by application code when constructing a new Order for insertion into persistent store. A new
     * Order number is generated automatically.
     *
     * @param customer
     *            The customer id to use for the order.
     * @param restaurant
     *            The restaurant id is used to identify the order restaurant
     * @param shipToAddress
     *            The ship address is to deliver the order
     * @param foods
     *            The food requested by customer to deliver
     */
    public OrderHeader newOrder (Customer customer, Restaurant restaurant,  Address shipToAddress, List<Food> foods)
    {
        List<RestaurantFood> restaurantFoods
                = restaurantFoodRepository.findAllRestaurantFoodByRestaurantAndFoodIn(restaurant,foods);
        RestaurantFood restaurantFood = restaurantFoodRepository.findTopByRestaurantAndFoodInOrderByEstimatedCookingTimeDesc(restaurant, foods);


        OrderHeader orderHeader = new OrderHeader();

        orderHeader.setCustomer(customer);
        orderHeader.setRestaurant(restaurant);
        orderHeader.setShipToAddress(shipToAddress);
        orderHeader.setPriority(orderHelper.calculatePriority(customer));
        orderHeader.setExpectedCookingTime(LocalDateTime.now().plusMinutes(!ObjectUtils.isEmpty(restaurantFood) ? restaurantFood.getEstimatedCookingTime().intValue() : 0));
        orderHeader.setExpectedPickupTime(orderHeader.getExpectedCookingTime());
        orderHeader.setStatus(OrderHeader.OrderHeaderStatus.RECEIVED);
        orderHeader.setOrderNumber(orderHelper.generateOrderNumber(restaurant));
        restaurantFoods.forEach(rf -> orderHeader.getOrderSegments().add(SegmentFactory.getInstance().newSegment(orderHeader, rf)));

        return orderHeader;
    }

    /**
     * Factory method for use by application code when constructing a new Order for insertion into persistent store. A new
     * Order number is generated automatically.
     *
     * @param addressModel
     *            The customer id to use for the order.
     *            The restaurant id is used to identify the order restaurant
     *            The ship address is to deliver the order
     *            The food requested by customer to deliver
     * @return  Address fetch in case of exists otherwise created address recor
     */
    private Address shipAddress(AddressModel addressModel)
    {
       if(StringUtils.hasText(addressModel.key()))
       {
           return addressRepository.findByKey(addressModel.key()).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase()));
       }
       else
       {
          Address address =addressFactory.newAddress(addressModel);
          return addressRepository.save(address);
       }
    }

}
