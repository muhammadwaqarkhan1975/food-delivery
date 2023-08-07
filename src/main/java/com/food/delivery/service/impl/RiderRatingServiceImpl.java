package com.food.delivery.service.impl;

import com.food.delivery.common.BeanUtil;
import com.food.delivery.domain.OrderHeader;
import com.food.delivery.domain.QRiderRating;
import com.food.delivery.domain.RiderRating;
import com.food.delivery.exception.ResourceNotFoundException;
import com.food.delivery.factory.RiderFactory;
import com.food.delivery.model.RiderRatingListModel;
import com.food.delivery.model.RiderRatingModel;
import com.food.delivery.repository.OrderHeaderRepository;
import com.food.delivery.repository.OrderTrackingRepository;
import com.food.delivery.repository.RiderRatingRepository;
import com.food.delivery.service.RiderRatingService;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RiderRatingServiceImpl implements RiderRatingService {

    private final RiderRatingRepository riderRatingRepository;
    private final OrderTrackingRepository orderTrackingRepository;
    private final RiderFactory riderFactory;

    @Override
    @Transactional
    public RiderRatingModel create(RiderRatingModel riderRatingModel)
    {
        OrderHeader orderHeader = BeanUtil.getBean(OrderHeaderRepository.class).findByKey(riderRatingModel.orderKey()).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase()));

        RiderRating riderRating = ObjectUtils.isEmpty(orderHeader.getOrderTracking())?
                riderFactory
                                    .newRiderRating(riderRatingModel)
                : riderFactory.newRiderRating(orderHeader, riderRatingModel.rating(), riderRatingModel.comment());

        riderRatingRepository.save(riderRating);
        return riderRatingModel;
    }

    @Override
    @Transactional
    public RiderRatingModel update(String key, RiderRatingModel riderRatingModel)
    {
        RiderRating riderRating = riderRatingRepository.findByKey(key).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase()));

        riderRating.setRating(riderRatingModel.rating());
        riderRating.setComment(riderRatingModel.comment());

        return riderRatingModel;
    }

    @Override
    public Page<RiderRatingListModel> fetchAll(String search, String riderKey, String orderKey, String customerKey, String restaurantKey, Pageable pageable) {

        BooleanBuilder filter = new BooleanBuilder();
        if (StringUtils.hasText(search))
            filter.and(QRiderRating.riderRating.comment.containsIgnoreCase(search));

        if (StringUtils.hasText(riderKey))
            filter.and(QRiderRating.riderRating.rider.key.eq(riderKey));

        if (StringUtils.hasText(orderKey))
            filter.and(QRiderRating.riderRating.orderHeader.key.eq(orderKey));

        if (StringUtils.hasText(customerKey))
            filter.and(QRiderRating.riderRating.customer.key.eq(orderKey));

        if (StringUtils.hasText(restaurantKey))
            filter.and(QRiderRating.riderRating.restaurant.key.eq(orderKey));

        return riderRatingRepository.findAll(filter,pageable).map(RiderRatingListModel::new);

    }

    @Override
    public RiderRatingListModel fetch(String key)
    {
        return new RiderRatingListModel(riderRatingRepository.findByKey(key).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase() , RiderRating.class.getName() + " " + key)));
    }
}
