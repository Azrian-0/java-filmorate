package ru.yandex.practicum.filmorate.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.EntityNotExist;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private UserService userService;
    @Autowired
    private UserValidator userValidator;
    private User user;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@example.com")
                .login("TestLogin")
                .name("Test name")
                .birthday(LocalDate.now().minusYears(1))
                .build();
        userService.create(user);

        user1 = User.builder()
                .email("test1@example.com")
                .login("TestLogin")
                .name("Test name")
                .birthday(LocalDate.now().minusYears(1))
                .build();
        userService.create(user1);

        user2 = User.builder()
                .email("test2@example.com")
                .login("TestLogin")
                .name("Test name")
                .birthday(LocalDate.now().minusYears(1))
                .build();
        userService.create(user2);
    }

    @Test
    void getById() {
        User result = userService.getById(1);
        assertEquals(user, result);
    }

    @Test
    public void deleteById() {
        userService.deleteById(1);
        EntityNotExist exception = assertThrows(EntityNotExist.class, () -> userService.getById(1));
        assertTrue(exception.getMessage().contains("Нет пользователя с таким id."));
    }

    @Test
    void addFriend() {
        userService.addFriend(1, 2);
        assertTrue(user.getFriends().contains(2));
        assertTrue(user1.getFriends().contains(1));
    }

    @Test
    void deleteFriend() {
        userService.addFriend(1, 2);
        userService.deleteFriend(1, 2);
        assertFalse(user.getFriends().contains(2));
        assertFalse(user1.getFriends().contains(1));
    }

    @Test
    void getAllFriends() {
        userService.addFriend(1, 2);

        List<User> friends = userService.getAllFriends(1);
        assertEquals(1, friends.size());
        assertTrue(friends.contains(user1));
    }

    @Test
    void getMutualFriends() {
        userService.addFriend(1, 2);
        userService.addFriend(1, 3);
        List<User> mutualFriends = userService.getMutualFriends(2, 3);

        assertEquals(1, mutualFriends.size());
        assertTrue(mutualFriends.contains(user));
    }
}