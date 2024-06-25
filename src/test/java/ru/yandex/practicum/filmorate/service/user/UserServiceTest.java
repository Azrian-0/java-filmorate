package ru.yandex.practicum.filmorate.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {
    
    private User user;
    private User user1;
    private User user2;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@example1.com")
                .login("TestLogin1")
                .name("Test name")
                .birthday(LocalDate.now().minusYears(1))
                .build();

        user1 = User.builder()
                .email("test1@example2.com")
                .login("TestLogin2")
                .name("Test name")
                .birthday(LocalDate.now().minusYears(1))
                .build();

        user2 = User.builder()
                .email("test1@example3.com")
                .login("TestLogin3")
                .name("Test name")
                .birthday(LocalDate.now().minusYears(1))
                .build();
    }

    @Test
    void create() {
        userService.create(user);
        User createdUser = userService.getById(user.getId());
        assertNotNull(createdUser);
        assertEquals(user.getId(), createdUser.getId());
    }

    @Test
    void getById() {
        userService.create(user);
        User createdUser = userService.getById(user.getId());
        assertNotNull(createdUser);
        assertEquals(user.getId(), createdUser.getId());
    }

    @Test
    void update() {
        userService.create(user);
        user.setName("New name");
        userService.update(user);
        User updatedUser = userService.getById(user.getId());
        assertNotNull(updatedUser);
        assertEquals(user.getName(), updatedUser.getName());
    }

    @Test
    void deleteById() {
        userService.create(user);
        int userId = user.getId();
        userService.deleteById(userId);
        Executable executable = () -> userService.getById(userId);
        assertThrows(EntityNotFoundException.class, executable);
    }

    @Test
    void getAll() {
        userService.create(user);
        userService.create(user1);
        Set<User> users = userService.getAll();
        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertTrue(users.contains(user));
        assertTrue(users.contains(user1));
    }

    @Test
    void addFriend() {
        userService.create(user);
        userService.create(user1);
        userService.addFriend(user.getId(), user1.getId());
        assertEquals(1, userService.getFriends(user.getId()).size());
    }

    @Test
    void deleteFriend() {
        userService.create(user);
        userService.create(user1);
        userService.addFriend(user.getId(), user1.getId());
        userService.deleteFriend(user.getId(), user1.getId());
        assertEquals(0, userService.getFriends(user.getId()).size());
    }

    @Test
    void getFriends() {
        userService.create(user);
        userService.create(user1);
        userService.addFriend(user.getId(), user1.getId());
        Set<User> foundFriends = userService.getFriends(user.getId());
        assertTrue(foundFriends.contains(user1));
    }

    @Test
    void getMutualFriends() {
        userService.create(user);
        userService.create(user1);
        userService.create(user2);
        userService.addFriend(user.getId(), user2.getId());
        userService.addFriend(user1.getId(), user2.getId());
        Set<User> mutualFriends = userService.getMutualFriends(user.getId(), user1.getId());
        assertTrue(mutualFriends.contains(user2));
    }
}