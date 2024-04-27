package ru.yandex.practicum.filmorate.exception;

public class ResourceNotFoundException extends NotFoundException {
    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(final String message) {
        super(message);
    }

    public ResourceNotFoundException(final String message, Throwable cause) {
        super(message, cause);
    }
}