package ru.yandex.practicum.filmorate.exception;

public class EntityAlreadyExist extends RuntimeException {
    public EntityAlreadyExist(String message) {
        super(message);
    }
}