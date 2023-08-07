package com.food.delivery.factory;

import com.food.delivery.domain.*;
import com.food.delivery.exception.ResourceNotFoundException;
import com.food.delivery.model.RiderRatingModel;
import com.food.delivery.model.UserAccountModel;
import com.food.delivery.repository.CustomerRepository;
import com.food.delivery.repository.OrderHeaderRepository;
import com.food.delivery.repository.RestaurantRepository;
import com.food.delivery.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RiderFactory
{
    private final  OrderHeaderRepository orderHeaderRepository;
    private final  UserAccountRepository userAccountRepository;
    private final  CustomerRepository customerRepository;
    private final  RestaurantRepository restaurantRepository;

    public UserAccount newRider (UserAccountModel userAccountModel)
    {
        return new RiderBuilder().withLastName(userAccountModel.lastName())
                .withFirstName(userAccountModel.firstName())
                .withPhoneNumber(userAccountModel.phoneNumber())
                .withPhoneNumber(userAccountModel.phoneNumber()).build();
    }

    /**
     * Factory method for use by application code when constructing a new rider rating for insertion into persistent store. A new
     * Order number is generated automatically.
     *
     * @param orderHeader
     *            The customer id to use for the order.
     * @param rating
     *            The rating to rider provided by the customer
     * @param comment
     *            Comments provided by the customer to rider
     * @return  RiderRating
     *            The rider rating
     */
    public RiderRating newRiderRating (OrderHeader orderHeader, double rating, String comment)
    {

        return newRiderRating(orderHeader, orderHeader.getCustomer(), orderHeader.getRestaurant(), orderHeader.getOrderTracking().getUserAccount(), rating, comment);
    }

    public RiderRating newRiderRating (OrderHeader orderHeader, Customer customer, Restaurant restaurant, UserAccount rider, double rating, String comment)
    {
        RiderRating riderRating = new RiderRating();
        riderRating.setRider(rider);
        riderRating.setOrderHeader(orderHeader);
        riderRating.setCustomer(customer);
        riderRating.setRestaurant(restaurant);
        riderRating.setRating(rating);
        riderRating.setComment(comment);
        return riderRating;
    }
    public RiderRating newRiderRating (RiderRatingModel riderRatingModel)
    {
        OrderHeader orderHeader = orderHeaderRepository.findByKey(riderRatingModel.orderKey()).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase()));
        UserAccount userAccount = userAccountRepository.findByKey(riderRatingModel.riderKey()).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase()));
        Customer customer = customerRepository.findByKey(riderRatingModel.customerKey()).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase()));
        Restaurant restaurant = restaurantRepository.findByKey(riderRatingModel.restaurantKey()).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase()));

        return newRiderRating(orderHeader, customer, restaurant, userAccount, riderRatingModel.rating(), riderRatingModel.comment());
    }

    public OrderTracking newOrderTracking (Long riderId, Long orderId)
    {
        return newOrderTracking(riderId, orderId, 0.0);
    }
    public OrderTracking newOrderTracking (Long riderId, Long orderId, double distance)
    {
        UserAccount userAccount = userAccountRepository.findById(riderId).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase()));
        OrderHeader orderHeader = orderHeaderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase()));
        return newOrderTracking(userAccount, orderHeader, distance);
    }

    public OrderTracking newOrderTracking (UserAccount rider, OrderHeader orderHeader,  double distance)
    {
        OrderTracking orderTracking = new OrderTracking();
        orderTracking.setUserAccount(rider);
        orderTracking.setOrderHeader(orderHeader);
        orderTracking.setLatitude(orderHeader.getShipToAddress().getLatitude());
        orderTracking.setLongitude(orderHeader.getShipToAddress().getLongitude());
        orderTracking.setDistance(distance);
        return orderTracking;
    }

    public RiderLocation newRiderLocation (String riderKey, Double latitude, Double longitude)
    {
        return newRiderLocation(userAccountRepository.findByKey(riderKey).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase())), latitude, longitude );
    }

    public RiderLocation newRiderLocation (UserAccount rider, Double latitude, Double longitude)
    {
        RiderLocation riderLocation = new RiderLocation();
        riderLocation.setRider(rider);
        riderLocation.setLatitude(latitude);
        riderLocation.setLongitude(longitude);
        return riderLocation;
    }


    private static class RiderBuilder {
        private String email;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private RiderBuilder() {
        }


        public RiderBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public RiderBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public RiderBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public RiderBuilder withPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }
        public UserAccount build()
        {
            UserAccount userAccount = new UserAccount();
            userAccount.setType(UserAccount.UserAccountType.RIDER);
            userAccount.setEmail(this.email);
            userAccount.setFirstName(this.firstName);
            userAccount.setLastName(this.lastName);
            userAccount.setStatus(UserAccount.UserAccountStatus.ACTIVE);
            userAccount.setPhoneNumber(this.phoneNumber);

            return userAccount;
        }
    }



}
