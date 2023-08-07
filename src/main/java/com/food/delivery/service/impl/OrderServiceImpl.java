package com.food.delivery.service.impl;


import com.food.delivery.common.OrderHelper;
import com.food.delivery.domain.OrderHeader;
import com.food.delivery.domain.OrderSegment;
import com.food.delivery.domain.OrderTracking;
import com.food.delivery.domain.QOrderHeader;
import com.food.delivery.exception.ResourceNotFoundException;
import com.food.delivery.factory.CustomerAddressFactory;
import com.food.delivery.factory.OrderFactory;
import com.food.delivery.mappers.OrderHeaderMapStructMapper;
import com.food.delivery.model.OrderHeaderModel;
import com.food.delivery.model.OrderModel;
import com.food.delivery.model.OrderTicketModel;
import com.food.delivery.repository.CustomerAddressRepository;
import com.food.delivery.repository.FoodDeliveryRepository;
import com.food.delivery.repository.OrderHeaderRepository;
import com.food.delivery.repository.RestaurantFoodRepository;
import com.food.delivery.service.OrderService;
import com.food.delivery.service.RiderService;
import com.querydsl.core.BooleanBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl  implements OrderService {

    private final OrderHeaderRepository orderHeaderRepository;
    private final OrderHeaderMapStructMapper orderHeaderMapStructMapper;
    private final CustomerAddressRepository customerAddressRepository;
    private final RestaurantFoodRepository restaurantFoodRepository;
    private final RiderService riderService;
    private final OrderHelper orderHelper;
    private final OrderFactory orderFactory;
    private final FoodDeliveryRepository foodDeliveryRepository;

    @Override
    @Transactional
    public OrderTicketModel create(OrderModel orderModel)
    {
        /*
            Step-1 -> Delivery Id ->  Order number is human-readable field,
            Step-2 -> Customer Type( VIP, Loyal, New), priority will be set on the base of customer type
            Step-3 -> Delivery Status, Initially order status would be received and will be change on the base of action performed on order
            Step-4 -> Expected time of delivery, each restaurant has food cooking time in restaurant_food table to identify the expected time,
                      NOTE This calculation of this field depend on how much capacity and staff do we have.
        */
        OrderHeader orderHeader =  orderFactory.newOrder(orderModel);
        orderHeaderRepository.save(orderHeader);

        //Has address registered against the customer
        addCustomerAddress(orderHeader);


        //Step-5 Find the suitable rider on the base of minimum distance and create order track record to monitor the live estimated time
        OrderTracking orderTracking = riderService.addOrderTracking(orderHeader);


        //Step-6 Rider rating-> fetch the required rider rating
        Double riderRating = !ObjectUtils.isEmpty(orderTracking) ? riderService.ridingRating(orderTracking.getUserAccount()) : 0.0;

        //Step-7 Restaurant mean time to prepare food,
        Double restaurantMeanTime =  restaurantFoodRepository.findTopByRestaurantAndFoodInOrderByEstimatedCookingTimeDesc(orderHeader.getRestaurant(),
                orderHeader.getOrderSegments().stream().map(OrderSegment::getFood).collect(Collectors.toList())).getEstimatedCookingTime();


        //Step-8 Time to reach the destination
        orderHeader.setExpectedDeliveryTime(orderHelper.expectedDeliveryTime(orderHeader.getExpectedCookingTime(), !ObjectUtils.isEmpty(orderTracking) ? orderTracking.getDistance() : 0.0));
        orderHeaderRepository.save(orderHeader);
        OrderTicketModel orderTicketModel  = new OrderTicketModel(orderHeader, riderRating, !ObjectUtils.isEmpty(orderTracking) ? orderTracking.getDistance() : 0.0, restaurantMeanTime);

        foodDeliveryRepository.save(orderFactory.newOrderFoodDelivery(orderTicketModel));

       return orderTicketModel;
    }

    @Override
    public Page<OrderHeaderModel> fetchAll(String search, Integer priority, OrderHeader.OrderHeaderStatus status,
                                           List<String> customerKeys, List<String> restaurantKeys, Pageable pageable)
    {
        BooleanBuilder filter = new BooleanBuilder();
        if (StringUtils.hasText(search))
            filter.and(QOrderHeader.orderHeader.orderNumber.containsIgnoreCase(search));

        if (!ObjectUtils.isEmpty(priority))
            filter.and(QOrderHeader.orderHeader.priority.eq(priority));

        if (!ObjectUtils.isEmpty(status))
            filter.and(QOrderHeader.orderHeader.status.eq(status));

        if (!ObjectUtils.isEmpty(customerKeys))
            filter.and(QOrderHeader.orderHeader.customer.key.in(customerKeys));

        if (!ObjectUtils.isEmpty(restaurantKeys))
            filter.and(QOrderHeader.orderHeader.restaurant.key.in(restaurantKeys));

        return orderHeaderRepository.findAll(filter,pageable).map(orderHeader -> orderHeaderMapStructMapper.model(orderHeader));
    }

    @Override
    public OrderHeaderModel fetch(String key)
    {
        return orderHeaderMapStructMapper.model(orderHeaderRepository.findByKey(key).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public void changeStatus(String key, OrderHeader.OrderHeaderStatus status) {

        OrderHeader orderHeader = orderHeaderRepository.findByKey(key).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase()));
        orderHeader.setStatus(status);

        orderHeaderRepository.save(orderHeader);

    }

    private void addCustomerAddress(OrderHeader orderHeader)
    {
        if(customerAddressRepository.existsByCustomerAndAddress(orderHeader.getCustomer(), orderHeader.getShipToAddress()))
            customerAddressRepository.save(CustomerAddressFactory.getInstance().newCustomerAddress(orderHeader.getCustomer(), orderHeader.getShipToAddress()));
    }



}
