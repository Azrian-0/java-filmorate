package ru.yandex.practicum.filmorate.exception;

public class EntityAlreadyExist extends BadRequestException {
    public EntityAlreadyExist(String message) {
        super(message);
    }
}