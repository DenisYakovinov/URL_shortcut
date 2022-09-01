package ru.job4j.shortcut.exception;

public class UrlValueReservedException extends ServiceException {

    public UrlValueReservedException(String message) {
        super(message);
    }

    public UrlValueReservedException(String message, Throwable cause) {
        super(message, cause);
    }
}