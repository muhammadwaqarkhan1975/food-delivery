package com.food.delivery.mappers;

import com.food.delivery.domain.OrderHeader;
import com.food.delivery.model.OrderHeaderModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {CustomerMapStructMapper.class},componentModel = "spring")
public interface OrderHeaderMapStructMapper
{

    @Mapping(source = "orderHeader.customer", target = "customerModel")
    @Mapping(source = "orderHeader.restaurant", target = "restaurantModel")
    @Mapping(source = "orderHeader.shipToAddress", target = "shipToAddress")
    @Mapping(source = "orderHeader.orderSegments", target = "orderSegments")
    OrderHeaderModel model(OrderHeader orderHeader);


    @Mapping(source = "orderHeaderModel.customerModel", target = "customer")
    @Mapping(source = "orderHeaderModel.restaurantModel", target = "restaurant")
    @Mapping(source = "orderHeaderModel.shipToAddress", target = "shipToAddress")
    @Mapping(source = "orderHeaderModel.orderSegments", target = "orderSegments")
    OrderHeader entity(OrderHeaderModel orderHeaderModel);

}
