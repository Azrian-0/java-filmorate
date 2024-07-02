package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @GetMapping
    public Set<User> getAll() {
        log.info("Обработан GET users запрос.");
        return userService.getAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Обработан POST user запрос.");
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обработан PUT user запрос.");
        return userService.update(user);
    }

    @GetMapping("/{userId}")
    public User getById(@PathVariable Integer userId) {
        log.info("Обработан GET user {} запрос.", userId);
        return userService.getById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        log.info("Обработан DELETE user {} запрос.", userId);
        userService.deleteById(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("Обработан PUT user {} запрос.", userId);
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User deleteFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("Обработан DELETE user {} friend запрос.", userId);
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public Set<User> getUserFriends(@PathVariable Integer userId) {
        log.info("Обработан GET user {} friends запрос.", userId);
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public Set<User> getMutualFriends(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("Обработан GET user {} mutual friends запрос.", userId);
        return userService.getMutualFriends(userId, friendId);
    }
}