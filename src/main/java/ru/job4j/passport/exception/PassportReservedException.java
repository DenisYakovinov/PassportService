package ru.job4j.passport.exception;

public class PassportReservedException extends RuntimeException {

    public PassportReservedException(String message) {
        super(message);
    }

    public PassportReservedException(String message, Throwable cause) {
        super(message, cause);
    }
}