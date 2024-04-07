package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private UserValidator validator;
    private User user;

    @BeforeEach
    public void setUp() {
        validator = new UserValidator();
        user = User.builder()
                .email("test@example.com")
                .login("TestLogin")
                .name("Test name")
                .birthday(LocalDate.now().minusYears(1))
                .build();
    }

    @Test
    public void testValidUser() {
        assertDoesNotThrow(() -> validator.validate(user));
    }

    @Test
    public void testInvalidEmail() {
        user.setEmail("invalid_email");
        ValidationException exception = assertThrows(ValidationException.class, () -> validator.validate(user));
        assertTrue(exception.getMessage().contains("Неверный формат электронной почты."));
    }

    @Test
    public void testBlankEmail() {
        user.setEmail("");
        ValidationException exception = assertThrows(ValidationException.class, () -> validator.validate(user));
        assertTrue(exception.getMessage().contains("Электронная почта не может быть пустой."));
    }

    @Test
    public void testInvalidLogin() {
        user.setLogin("login with spaces");
        ValidationException exception = assertThrows(ValidationException.class, () -> validator.validate(user));
        assertTrue(exception.getMessage().contains("Логин не может содержать пробелы."));
    }

    @Test
    public void testBlankLogin() {
        user.setLogin("");
        ValidationException exception = assertThrows(ValidationException.class, () -> validator.validate(user));
        assertTrue(exception.getMessage().contains("Логин не может быть пустым."));
    }

    @Test
    public void testInvalidBirthday() {
        user.setBirthday(LocalDate.now().plusDays(1));
        ValidationException exception = assertThrows(ValidationException.class, () -> validator.validate(user));
        assertTrue(exception.getMessage().contains("Дата рождения не может быть в будущем."));
    }

    @Test
    public void testBlankBirthday() {
        user.setBirthday(null);
        ValidationException exception = assertThrows(ValidationException.class, () -> validator.validate(user));
        assertTrue(exception.getMessage().contains("Дата рождения не может быть пустой."));
    }
}