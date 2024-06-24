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
import java.util.stream.Collectors;

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
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotExist("Нет пользователя с таким id."));
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

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        getById(userId).getFriends().add(friendId);
        getById(friendId).getFriends().add(userId);
        log.info("Пользователи {} и {} добавлены в друзья.", getById(userId).getLogin(), getById(friendId).getLogin());
        return getById(userId);
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        getById(userId).getFriends().remove(friendId);
        getById(friendId).getFriends().remove(userId);
        log.info("Пользователи {} и {} удалены из друзей.", getById(userId).getLogin(), getById(friendId).getLogin());
        return getById(userId);
    }

    @Override
    public Set<User> getFriends(Integer userId) {
        return users.stream()
                .filter(user -> user.getFriends().contains(userId))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getMutualFriends(Integer userId, Integer friendId) {
        Set<User> mutualFriends = new HashSet<>();
        for (Integer id : getById(userId).getFriends()) {
            if (getById(friendId).getFriends().contains(id)) {
                mutualFriends.add(getById(id));
            }
        }
        return mutualFriends;
    }
}