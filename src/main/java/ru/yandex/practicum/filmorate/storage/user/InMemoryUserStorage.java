package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExist;
import ru.yandex.practicum.filmorate.exception.EntityNotExist;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Set<User> users = new HashSet<>();

    private int nextId = 1;

    private int incrementId() {
        return nextId++;
    }

    @Override
    public User create(@Valid @RequestBody User user) {
        if (users.contains(user)) {
            throw new EntityAlreadyExist("Такой пользователь уже существует.");
        }
        if (users.stream()
                .filter(existingUser -> !existingUser.equals(user))
                .anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()))) {
            throw new EntityAlreadyExist("Пользователь с таким email уже существует.");
        }
        user.setId(incrementId());
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.add(user);
        log.info("Пользователь {} добавлен.", user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.contains(user)) {
            throw new EntityNotExist("Такого пользователя не существует.");
        }
        if (users.stream()
                .filter(existingUser -> !existingUser.equals(user))
                .anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()))) {
            throw new EntityAlreadyExist("Пользователь с таким email уже существует.");
        }
        users.remove(user);
        users.add(user);
        log.info("Пользователь {} обновлен.", user);
        return user;
    }

    @Override
    public Set<User> getAll() {
        return users;
    }

    @Override
    public User getById(Integer id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        throw new EntityNotExist("Нет пользователя с таким id.");
    }

    @Override
    public void deleteById(Integer id) {
        User userToRemove = getById(id);
        users.remove(userToRemove);
        log.info("Пользователь {} удален.", userToRemove.getEmail());
    }

    @Override
    public boolean isUserExist(Integer userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }
}