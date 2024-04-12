package ru.yandex.practicum.filmorate.exception;

public class EntityNotExist extends NotFoundException {
    public EntityNotExist(String message) {
        super(message);
    }
}