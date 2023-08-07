package com.food.delivery.factory;

import com.food.delivery.domain.Address;
import com.food.delivery.domain.Customer;
import com.food.delivery.domain.CustomerAddress;

public class CustomerAddressFactory
{

    private static final CustomerAddressFactory FACTORY	= new CustomerAddressFactory();

    /**
     * Returns an instance of the factory
     */
    public static CustomerAddressFactory getInstance ()
    {
        return FACTORY;
    }

    private CustomerAddressFactory()
    {
    }

    /*
     * @param  customer
     * @param  address
     * @return      customer address for insertion into persistent store
     * @see         CustomerAddress
     */
    public CustomerAddress newCustomerAddress (Customer customer, Address address)
    {
        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setAddress(address);
        customerAddress.setCustomer(customer);
        return customerAddress;
    }


}
