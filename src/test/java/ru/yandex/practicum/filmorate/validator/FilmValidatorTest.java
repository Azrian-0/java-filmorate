package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {

    private FilmValidator validator;

    private Film film;

    @BeforeEach
    public void setUp() {
        validator = new FilmValidator();
        film = Film.builder()
                .name("First Test Film")
                .description("Test description")
                .releaseDate(LocalDate.now().minusYears(1))
                .duration(120)
                .build();
        film.setGenres(new LinkedHashSet<>());
        film.setLikes(new LinkedHashSet<>());
        film.setMpa(Mpa.builder()
                .id(1)
                .name("G")
                .build());
    }

    @Test
    public void testValidFilm() {
        assertDoesNotThrow(() -> validator.validate(film));
    }

    @Test
    public void testInvalidName() {
        film.setName("");
        ValidationException exception = assertThrows(ValidationException.class, () -> validator.validate(film));
        assertTrue(exception.getMessage().contains("Название фильма не может быть пустым."));
    }

    @Test
    public void testInvalidDescription() {
        film.setDescription("X".repeat(201));
        ValidationException exception = assertThrows(ValidationException.class, () -> validator.validate(film));
        assertTrue(exception.getMessage().contains("Длина описания фильма не должна превышать 200 символов."));
    }

    @Test
    public void testInvalidReleaseDate() {
        film.setReleaseDate(LocalDate.of(1800, 1, 1));
        ValidationException exception = assertThrows(ValidationException.class, () -> validator.validate(film));
        assertTrue(exception.getMessage().contains("Дата релиза фильма должна быть не раньше 28 декабря 1895 года."));
    }

    @Test
    public void testInvalidDuration() {
        film.setDuration(-10);
        ValidationException exception = assertThrows(ValidationException.class, () -> validator.validate(film));
        assertTrue(exception.getMessage().contains("Продолжительность фильма должна быть положительной."));
    }
}