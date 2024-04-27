package ru.yandex.practicum.filmorate.validator;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Component
public class FilmValidator {

    private final Validator validator;

    public FilmValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public void validate(Film film) throws ValidationException {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<Film> violation : violations) {
                errorMessage.append(violation.getMessage()).append("\n");
            }
            throw new ValidationException(errorMessage.toString());
        }
    }
}