package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator {

    public void validate(User user) throws ValidationException {
        if (!isEmailValid(user.getEmail())) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (!isLoginValid(user.getLogin())) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (!isNameValid(user.getName(), user.getLogin())) {
            throw new ValidationException("Имя не указано — использован логин.");
        }
        if (!isBirthdayValid(user.getBirthday())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

    private boolean isEmailValid(String email) {
        return email != null && email.contains("@");
    }

    private boolean isLoginValid(String login) {
        return login != null && !login.contains(" ");
    }

    private boolean isNameValid(String name, String login) {
        if (name == null || name.isEmpty()) {
            return isLoginValid(login);
        } else {
            return true;
        }
    }

    private boolean isBirthdayValid(LocalDate birthday) {
        return !birthday.isAfter(LocalDate.now());
    }
}