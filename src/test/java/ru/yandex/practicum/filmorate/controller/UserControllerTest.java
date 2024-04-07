package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExist;
import ru.yandex.practicum.filmorate.exception.EntityNotExist;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {
    @Autowired
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@example.com")
                .login("TestLogin")
                .name("Test name")
                .birthday(LocalDate.now().minusYears(1))
                .build();
    }

    @Test
    void testFindAll() {
        User createdUser = userController.create(user);
        Set<User> users = userController.findAll();
        assertThat(users).contains(createdUser);
    }

    @Test
    void testCreateUser() {
        User createdUser = userController.create(user);
        assertThat(createdUser).isEqualTo(user);
    }

    @Test
    void testUpdateUser() {
        User createdUser = userController.create(user);
        createdUser.setEmail("new@example.com");
        User updatedUser = userController.update(createdUser);
        assertThat(updatedUser).isEqualTo(createdUser);
    }

    @Test
    void testCreateDuplicateUser() {
        userController.create(user);
        assertThrows(EntityAlreadyExist.class, () -> userController.create(user));
    }

    @Test
    void testUpdateNonexistentUser() {
        assertThrows(EntityNotExist.class, () -> userController.update(user));
    }
}