package com.reliaquest.api.controller.exception;

public class EmployeeValidationException extends Exception {
    public EmployeeValidationException() {
        super();
    }

    public EmployeeValidationException(String message) {
        super(message);
    }

    public EmployeeValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmployeeValidationException(Throwable cause) {
        super(cause);
    }
}
