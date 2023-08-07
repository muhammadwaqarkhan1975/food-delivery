package com.food.delivery.exception;

public class FoodDeliveryException extends RuntimeException{

    public FoodDeliveryException(String errorCode, String errorMessage) {
        super(String.format(errorCode + ". " + errorMessage));
    }

    public FoodDeliveryException(String errorMessage) {
        super(errorMessage);
    }
}