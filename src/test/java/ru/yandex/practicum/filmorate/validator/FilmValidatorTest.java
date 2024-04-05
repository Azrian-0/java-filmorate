package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {
    private FilmValidator filmValidator;
    private Film film;

    @BeforeEach
    public void setup() {
        filmValidator = new FilmValidator();
        film = Film.builder()
                .name("Test Film")
                .description("Test description")
                .releaseDate(LocalDate.now())
                .duration(120)
                .build();
    }

    @Test
    public void testNameValidation() {
        film.setName("");
        ValidationException exception = assertThrows(ValidationException.class, () -> filmValidator.validate(film));
        assertNotNull(exception);
        assertEquals("Название фильма не может быть пустым.", exception.getMessage());
    }

    @Test
    public void testDescriptionValidation() {
        film.setDescription("X".repeat(201));
        ValidationException exception = assertThrows(ValidationException.class, () -> filmValidator.validate(film));
        assertNotNull(exception);
        assertEquals("Длина описания фильма не должна превышать 200 символов.", exception.getMessage());
    }

    @Test
    public void testReleaseDateValidation() {
        film.setReleaseDate(LocalDate.of(1895, 1, 1));
        ValidationException exception = assertThrows(ValidationException.class, () -> filmValidator.validate(film));
        assertNotNull(exception);
        assertEquals("Дата релиза фильма должна быть не раньше 28 декабря 1895 года.", exception.getMessage());
    }

    @Test
    public void testDurationValidation() {
        film.setDuration(-1);
        ValidationException exception = assertThrows(ValidationException.class, () -> filmValidator.validate(film));
        assertNotNull(exception);
        assertEquals("Продолжительность фильма должна быть положительной.", exception.getMessage());
    }
}