package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private UserValidator userValidator;
    private User user;

    @BeforeEach
    public void setup() {
        userValidator = new UserValidator();
        user = User.builder()
                .email("Test email @")
                .login("TestLogin")
                .name("Test name")
                .birthday(LocalDate.now())
                .build();
    }

    @Test
    public void testEmailValid() {
        user.setEmail("");
        ValidationException exception = assertThrows(ValidationException.class, () -> userValidator.validate(user));
        assertNotNull(exception);
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    public void testLoginValid() {
        user.setLogin(" ");
        ValidationException exception = assertThrows(ValidationException.class, () -> userValidator.validate(user));
        assertNotNull(exception);
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    public void testNameValid() {
        user.setName("");
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        assertDoesNotThrow(() -> userValidator.validate(user));
        assertEquals("TestLogin", user.getName());
    }

    @Test
    public void testBirthdayValid() {
        user.setBirthday(LocalDate.now().plusYears(5));
        ValidationException exception = assertThrows(ValidationException.class, () -> userValidator.validate(user));
        assertNotNull(exception);
        assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }
}