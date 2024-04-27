package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        userValidator.validate(user);
        return userStorage.update(user);
    }

    public Set<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(Integer id) {
        return userStorage.getById(id);
    }

    public void deleteById(Integer id) {
        userStorage.deleteById(id);
    }

    public User addFriend(Integer userId, Integer friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.info("Пользователю c id: {} добавлен в друзья пользователь с id: {}.", userId, friendId);
        return user;
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        getById(userId).getFriends().remove(friendId);
        getById(friendId).getFriends().remove(userId);
        log.info("Пользователю c id: {} удалён из друзей пользователь с id: {}.", userId, friendId);
        return getById(userId);
    }

    public List<User> getAllFriends(Integer userId) {
        return getById(userId).getFriends().stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    public List<User> getMutualFriends(Integer userId, Integer friendId) {
        Set<Integer> mutualFriends = new HashSet<>(getById(userId).getFriends());
        mutualFriends.retainAll(getById(friendId).getFriends());
        return mutualFriends.stream().map(this::getById).collect(Collectors.toList());
    }
}