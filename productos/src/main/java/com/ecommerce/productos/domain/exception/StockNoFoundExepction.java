package com.ecommerce.productos.domain.exception;

public class StockNoFoundExepction extends RuntimeException {
    public StockNoFoundExepction(String message) {
        super(message);
    }
}
