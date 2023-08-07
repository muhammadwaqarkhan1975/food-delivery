package com.food.delivery.mappers;

import com.food.delivery.domain.UserAddress;
import com.food.delivery.model.UserAddressModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {AddressMapStructMapper.class},componentModel = "spring")
public interface UserAddressMapStructMapper
{
    @Mapping(source = "userAddress.address", target = "addressModel")
    UserAddressModel model(UserAddress userAddress);

   @Mapping(source = "userAddressModel.addressModel", target = "address")
   @Mapping(source = "userAddressModel.userAccountModel", target = "userAccount")
    UserAddress entity(UserAddressModel userAddressModel);

}
