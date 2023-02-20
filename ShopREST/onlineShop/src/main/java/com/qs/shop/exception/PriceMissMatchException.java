package com.qs.shop.exception;

public class PriceMissMatchException extends RuntimeException{


    public PriceMissMatchException(String message){
        super(String.format(message));

    }
}