package ru.job4j.passport.exception;

import org.hibernate.service.spi.ServiceException;

public class ServiceValidateException extends ServiceException {

    public ServiceValidateException(String message) {
        super(message);
    }

    public ServiceValidateException(String message, Throwable cause) {
        super(message, cause);
    }
}