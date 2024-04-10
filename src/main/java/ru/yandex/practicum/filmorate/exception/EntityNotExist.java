package ru.yandex.practicum.filmorate.exception;

public class EntityNotExist extends RuntimeException {
    public EntityNotExist(String message) {
        super(message);
    }
}