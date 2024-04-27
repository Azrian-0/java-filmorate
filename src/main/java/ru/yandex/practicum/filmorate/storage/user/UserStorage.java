package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface UserStorage {

    User create(User user);

    User update(User user);

    Set<User> getAll();

    User getById(Integer id);

    void deleteById(Integer id);

    boolean isUserExist(Integer id);
}