package com.food.delivery.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends FoodDeliveryException {

    public ResourceNotFoundException(String resourceName, String reference, HttpStatus status) {
        super(status.getReasonPhrase() + ": Unable to find resource "+resourceName+" with reference "+ reference );
    }

    public ResourceNotFoundException(String resourceName, String reference) {
        super("Unable to find resource {" + resourceName + "} with reference {" + reference + "}");
    }

    public ResourceNotFoundException(String reason) {
        super(reason);

    }
}
