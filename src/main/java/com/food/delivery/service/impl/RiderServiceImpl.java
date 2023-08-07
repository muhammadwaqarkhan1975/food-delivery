package com.food.delivery.service.impl;

import com.food.delivery.domain.OrderHeader;
import com.food.delivery.domain.OrderTracking;
import com.food.delivery.domain.RiderRating;
import com.food.delivery.domain.UserAccount;
import com.food.delivery.exception.ResourceNotFoundException;
import com.food.delivery.factory.RiderFactory;
import com.food.delivery.model.AvailableRider;
import com.food.delivery.repository.OrderTrackingRepository;
import com.food.delivery.repository.RiderLocationRepository;
import com.food.delivery.repository.RiderRatingRepository;
import com.food.delivery.repository.UserAccountRepository;
import com.food.delivery.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService
{
    private final RiderLocationRepository riderLocationRepository;
    private final UserAccountRepository userAccountRepository;
    private final OrderTrackingRepository orderTrackingRepository;
    private final RiderRatingRepository riderRatingRepository;
    private final RiderFactory riderFactory;


    @Override
    public OrderTracking addOrderTracking(OrderHeader orderHeader)
    {
        List<AvailableRider> availableRiders = riderLocationRepository.findNearestRider(orderHeader.getRestaurant().getAddress().getLatitude(), orderHeader.getRestaurant().getAddress().getLongitude());

        if(!ObjectUtils.isEmpty(availableRiders))
        {
            AvailableRider availableRider = availableRiders.iterator().next();
            UserAccount userAccount = userAccountRepository.findById(availableRider.riderId()).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase()));
            OrderTracking orderTracking = riderFactory.newOrderTracking(userAccount, orderHeader, availableRider.distance());
           return orderTrackingRepository.save(orderTracking);

        }
        return null;
    }

    @Override
    public Double ridingRating(UserAccount userAccount)
    {
        List<RiderRating> riderRatings = riderRatingRepository.findAllByRider(userAccount);
        if(riderRatings.size() == 0)
            return 0.0;
        return riderRatings.stream().mapToDouble(RiderRating::getRating).sum()/riderRatings.size();
    }
}
