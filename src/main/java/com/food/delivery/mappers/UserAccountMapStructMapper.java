package com.food.delivery.mappers;

import com.food.delivery.domain.UserAccount;
import com.food.delivery.model.UserAccountModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {UserAddressMapStructMapper.class},componentModel = "spring")
public interface UserAccountMapStructMapper
{
    @Mapping(source = "userAccount.userAddresses", target = "userAddressModels")
    UserAccountModel model(UserAccount userAccount);

    @Mapping(source = "userAccountModel.userAddressModels", target = "userAddresses")
    UserAccount entity(UserAccountModel userAccountModel);

}
