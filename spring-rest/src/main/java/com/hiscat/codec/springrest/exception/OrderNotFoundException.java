package com.hiscat.codec.springrest.exception;

/**
 * @author Administrator
 */
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Cloud not find order " + id);
    }
}
