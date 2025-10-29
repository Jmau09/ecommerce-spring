package com.ecommerce.productos.domain.exception;

public class CarritoNoFoundException extends RuntimeException {
    public CarritoNoFoundException(String message) {
        super(message);
    }
}
