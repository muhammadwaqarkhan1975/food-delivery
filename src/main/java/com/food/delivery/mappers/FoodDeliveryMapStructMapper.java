package com.food.delivery.mappers;

import com.food.delivery.domain.FoodDelivery;
import com.food.delivery.model.FoodDeliveryModel;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface FoodDeliveryMapStructMapper
{
    FoodDeliveryModel model(FoodDelivery foodDelivery);

    FoodDelivery entity(FoodDeliveryModel foodDeliveryModel);

}
