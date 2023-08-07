package com.food.delivery.common;

import com.food.delivery.domain.Customer;
import com.food.delivery.domain.FoodDelivery;
import com.food.delivery.domain.Restaurant;
import com.food.delivery.domain.SequenceNumber;
import com.food.delivery.repository.SequenceNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OrderHelper
{
    @Value("${spring.food.delivery.rider.avg.speed:25}")
    private int riderAvgSpeed;
    @Value("${spring.food.delivery.rider.rating.threshold:3}")
    private int ratingThreshold;

    private final SequenceNumberRepository sequenceNumberRepository;
    /*
        This method is responsible to calculate the priority of order on the base of customer.
     */
    public void calculatePriority(FoodDelivery foodDelivery)
    {

        foodDelivery.setPriority(Customer.CustomerType.fromString(foodDelivery.getCustomer()).getPriority());

        if(LocalDateTime.now().isAfter(foodDelivery.getExpectedDeliveryTime()))
            foodDelivery.setPriority(Customer.CustomerType.VIP.getPriority());

        //NOTE: EXTRA If rider rating is high we can make his work as high priority for his motivation
        if(foodDelivery.getRiderRating() >= ratingThreshold)
            foodDelivery.setPriority(Customer.CustomerType.VIP.getPriority());


    }

    public boolean canDeliverOnTime(FoodDelivery foodDelivery)
    {
        if (foodDelivery.getActualDeliveryTime() == null || foodDelivery.getExpectedDeliveryTime() == null)
            return false;

        return foodDelivery.getActualDeliveryTime().isBefore(LocalDateTime.now()) && foodDelivery.getActualDeliveryTime().plusMinutes(foodDelivery.getFoodMeanTime().intValue()).isAfter(foodDelivery.getExpectedDeliveryTime());
    }

    public boolean isVipCustomer(String customer)
    {
        return Customer.CustomerType.VIP.toString().equalsIgnoreCase(customer);
    }

    public Integer calculatePriority(Customer customer)
    {
        return switch (customer.getType()) {
            case VIP   -> Customer.CustomerType.VIP.getPriority();
            case LOYAL -> Customer.CustomerType.LOYAL.getPriority();
            default    -> Customer.CustomerType.NEW.getPriority();
        };
    }

    /*
        This method is responsible to calculate the expected time through distance and rider speed.
        Distance calculation will be done live through rider location table, We are expecting that rider live location
        will be updated in rider location,   RiderLocationRepositoryCustomImpl.findNearestRider will help to find the
        nearest rider along with distance.

        TODO: Assumption is, We are calculating the expected time through configured speed (spring.food.delivery.rider.avg.speed)
             putting the todo to get the live update from the google api to make it more correct
     */
    public LocalDateTime expectedDeliveryTime(LocalDateTime cookingTime, double distance)
    {
        return cookingTime.minusHours(Double.valueOf(distance/riderAvgSpeed).longValue());
    }

    /*
        This method is responsible to generate the unique order number for human-readable form to track the order.
        It will be the unique identify of order header table.
        A new sequence has been generated in postgres for this purpose. order number will be generated on the base of restaurant code
    */
    public String generateOrderNumber(Restaurant restaurant)
    {
        SequenceNumber sequenceNumber = new SequenceNumber();
        sequenceNumber.setName(restaurant.getName());
       return restaurant.getCode() + "-"+sequenceNumberRepository.saveAndFlush(sequenceNumber).getWebId();
    }

    public static Pageable toPriorityPageable(int page, int size)
    {
        return PageRequest.of(0, size, Sort.Direction.ASC, "priority");
    }
}
