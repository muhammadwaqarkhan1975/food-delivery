package com.food.delivery.service.impl;

import com.food.delivery.domain.FoodDelivery;
import com.food.delivery.domain.QFoodDelivery;
import com.food.delivery.exception.ResourceNotFoundException;
import com.food.delivery.mappers.FoodDeliveryMapStructMapper;
import com.food.delivery.model.FoodDeliveryModel;
import com.food.delivery.repository.FoodDeliveryRepository;
import com.food.delivery.service.FoodDeliveryService;
import com.querydsl.core.BooleanBuilder;
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
public class FoodDeliveryServiceImpl implements FoodDeliveryService {

    private final FoodDeliveryRepository foodDeliveryRepository;
    private final FoodDeliveryMapStructMapper foodDeliveryMapStructMapper;
    @Override
    @Transactional
    public FoodDeliveryModel create(FoodDeliveryModel foodDeliveryModel) {

        return foodDeliveryMapStructMapper.model(foodDeliveryRepository.save(foodDeliveryMapStructMapper.entity(foodDeliveryModel)));
    }

    @Override
    public Page<FoodDeliveryModel> fetchAll(String search, String deliveryId, String customer, FoodDelivery.DeliveryStatus deliveryStatus, Integer priority, Pageable pageable) {

        BooleanBuilder filter = new BooleanBuilder();
        if (StringUtils.hasText(search))
        {
            filter.and(QFoodDelivery.foodDelivery.deliveryId.containsIgnoreCase(search));
            filter.and(QFoodDelivery.foodDelivery.customer.containsIgnoreCase(search));
        }

        if (StringUtils.hasText(deliveryId))
            filter.and(QFoodDelivery.foodDelivery.deliveryId.containsIgnoreCase(deliveryId));

        if (StringUtils.hasText(customer))
            filter.and(QFoodDelivery.foodDelivery.customer.containsIgnoreCase(customer));

        if (!ObjectUtils.isEmpty(deliveryStatus))
            filter.and(QFoodDelivery.foodDelivery.deliveryStatus.eq(deliveryStatus));

        if (!ObjectUtils.isEmpty(priority))
            filter.and(QFoodDelivery.foodDelivery.priority.eq(priority));


        return foodDeliveryRepository.findAll(filter,pageable).map(foodDeliveryMapStructMapper::model);
    }

    @Override
    public List<FoodDeliveryModel> fetchPriorityTicket() {
        return foodDeliveryRepository.findAllByOrderByPriorityAsc().stream().map(foodDeliveryMapStructMapper::model).collect(Collectors.toList());
    }

    @Override
    public FoodDeliveryModel fetch(String key) {
        return foodDeliveryMapStructMapper.model(foodDeliveryRepository.findByKey(key).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase())));
    }
}
