package com.food.delivery.mappers;

import com.food.delivery.domain.RiderLocation;
import com.food.delivery.model.RiderLocationModel;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface RiderLocationMapStructMapper
{
    RiderLocationModel model(RiderLocation riderLocation);

    RiderLocation entity(RiderLocationModel riderLocationModel);

}
