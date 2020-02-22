package com.hiscat.codec.springrest.exception;

/**
 * @author Administrator
 */
public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long id) {
        super("Cloud not find employee " + id);
    }
}
