package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
    }

    public NotFoundException(final String message) {
        super(message);
    }

    public NotFoundException(final String message, Throwable cause) {
        super(message, cause);
    }
}