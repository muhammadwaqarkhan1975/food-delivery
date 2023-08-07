package com.food.delivery.mappers;

import com.food.delivery.domain.Address;
import com.food.delivery.model.AddressModel;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface AddressMapStructMapper
{
    AddressModel model(Address address);

    Address entity(AddressModel addressModel);

}
