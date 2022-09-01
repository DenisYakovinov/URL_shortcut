package ru.job4j.shortcut.exception;

public class SiteNameReservedException extends ServiceException {

    public SiteNameReservedException(String message) {
        super(message);
    }

    public SiteNameReservedException(String message, Throwable cause) {
        super(message, cause);
    }
}