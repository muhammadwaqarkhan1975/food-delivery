package com.food.delivery.service.impl;

import com.food.delivery.common.BeanUtil;
import com.food.delivery.domain.RiderLocation;
import com.food.delivery.domain.Ticket;
import com.food.delivery.exception.ResourceNotFoundException;
import com.food.delivery.factory.RiderFactory;
import com.food.delivery.mappers.RiderLocationMapStructMapper;
import com.food.delivery.model.RiderLocationModel;
import com.food.delivery.model.TicketModel;
import com.food.delivery.repository.RiderLocationRepository;
import com.food.delivery.service.RiderLocationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RiderLocationServiceImpl implements RiderLocationService
{
    private final RiderLocationRepository riderLocationRepository;
    private final RiderLocationMapStructMapper riderLocationMapStructMapper;
    private final RiderFactory riderFactory;

    @Override
    @Transactional
    public RiderLocationModel create(RiderLocationModel riderLocationModel)
    {
        RiderLocation riderLocation = riderFactory.newRiderLocation(riderLocationModel.riderKey(), riderLocationModel.latitude(), riderLocationModel.longitude());
        return riderLocationMapStructMapper.model(riderLocationRepository.save(riderLocation));
    }

    @Override
    @Transactional
    public RiderLocationModel update(String key, RiderLocationModel riderLocationModel)
    {
        RiderLocation riderLocation = BeanUtil.getBean(RiderLocationRepository.class).findByKey(key).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND.getReasonPhrase()));
        riderLocation.setLongitude(riderLocationModel.longitude());
        riderLocation.setLatitude(riderLocationModel.latitude());

        return riderLocationMapStructMapper.model(riderLocationRepository.save(riderLocation));
    }

    @Override
    public Page<RiderLocationModel> fetchAll(String search, String riderKey, Pageable pageable) {
        return null;
    }

    @Override
    public RiderLocationModel fetch(String key) {
        return riderLocationMapStructMapper.model(riderLocationRepository.findByKey(key).orElseThrow(() -> new ResourceNotFoundException(Ticket.class.getSimpleName(), key , HttpStatus.NOT_FOUND)));
    }
}
