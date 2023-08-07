package com.food.delivery.mappers;

import com.food.delivery.domain.Customer;
import com.food.delivery.model.CustomerModel;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface CustomerMapStructMapper
{
    CustomerModel model(Customer customer);

    Customer entity(CustomerModel customerModel);
}
