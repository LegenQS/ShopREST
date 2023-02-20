package com.qs.shop.exception;

public class ProductNotExistException extends RuntimeException{
    public ProductNotExistException(String message) {
        super(String.format(message));
    }
}
