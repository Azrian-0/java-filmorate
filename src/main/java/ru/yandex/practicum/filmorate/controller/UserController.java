package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExist;
import ru.yandex.practicum.filmorate.exception.EntityNotExist;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserValidator userValidator = new UserValidator();
    private final Set<User> users = new LinkedHashSet<>();

    @GetMapping
    public Set<User> findAll() {
        return users;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (users.contains(user)) {
            throw new EntityAlreadyExist("Такой пользователь уже существует.");
        }
        if (users.stream().anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()))) {
            throw new EntityAlreadyExist("Пользователь с таким email уже существует.");
        }
        userValidator.validate(user);
        user.setId(User.incrementId());
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.add(user);
        log.info("Пользователь {} добавлен.", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!users.contains(user)) {
            throw new EntityNotExist("Такого пользователя не существует.");
        }
        if (users.stream().anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()))) {
            throw new EntityAlreadyExist("Пользователь с таким email уже существует.");
        }
        userValidator.validate(user);
        users.remove(user);
        users.add(user);
        log.info("Пользователь {} обновлен.", user);
        return user;
    }
}