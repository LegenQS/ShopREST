package com.qs.shop.exception;

public class NotEnoughInventoryException extends RuntimeException{


    public NotEnoughInventoryException(String message){
        super(String.format(message));

    }
}