package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    private final UserValidator userValidator;

    public User create(User user) {
        userValidator.validate(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        checkUserExist(user.getId());
        userValidator.validate(user);
        return userStorage.update(user);
    }

    public Set<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(Integer id) {
        checkUserExist(id);
        return userStorage.getById(id);
    }

    public void deleteById(Integer id) {
        checkUserExist(id);
        userStorage.deleteById(id);
    }

    public User addFriend(Integer userId, Integer friendId) {
        checkUserAndFriendExist(userId, friendId);
        return userStorage.addFriend(userId, friendId);
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        checkUserAndFriendExist(userId, friendId);
        return userStorage.deleteFriend(userId, friendId);
    }

    public Set<User> getFriends(Integer userId) {
        checkUserExist(userId);
        return userStorage.getFriends(userId);
    }

    public Set<User> getMutualFriends(Integer userId, Integer friendId) {
        checkUserAndFriendExist(userId, friendId);
        return userStorage.getMutualFriends(userId, friendId);
    }

    private void checkUserAndFriendExist(Integer userId, Integer friendId) {
        if (!userStorage.isUserExist(userId) || !userStorage.isUserExist(friendId)) {
            throw new EntityNotFoundException();
        }
    }

    private void checkUserExist(Integer id) {
        if (!userStorage.isUserExist(id)) {
            throw new EntityNotFoundException();
        }
    }
}