package ru.yandex.practicum.filmorate.exception;

public class EntityNotFoundException extends NotFoundException {
    public EntityNotFoundException() {
    }

    public EntityNotFoundException(final String message) {
        super(message);
    }

    public EntityNotFoundException(final String message, Throwable cause) {
        super(message, cause);
    }
}