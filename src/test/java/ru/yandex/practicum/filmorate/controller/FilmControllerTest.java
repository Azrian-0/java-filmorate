package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExist;
import ru.yandex.practicum.filmorate.exception.EntityNotExist;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmControllerTest {

    @Autowired
    private FilmController filmController;

    private Film film;

    @BeforeEach
    void setUp() {
        film = Film.builder()
                .name("Test Film")
                .description("Test description")
                .releaseDate(LocalDate.now().minusYears(1))
                .duration(120)
                .build();
    }

    @Test
    void testFindAll() {
        Film createdFilm = filmController.create(film);
        Set<Film> films = filmController.getAll();
        assertThat(films).contains(createdFilm);
    }

    @Test
    void testCreateFilm() {
        Film createdFilm = filmController.create(film);
        assertThat(createdFilm).isEqualTo(film);
    }

    @Test
    void testUpdateFilm() {
        Film createdFilm = filmController.create(film);
        createdFilm.setName("UpdatedName");
        Film updatedFilm = filmController.update(film);
        assertThat(updatedFilm).isEqualTo(createdFilm);
    }

    @Test
    void testCreateDuplicateFilm() {
        filmController.create(film);
        assertThrows(EntityAlreadyExist.class, () -> filmController.create(film));
    }

    @Test
    void testUpdateNonexistentFilm() {
        assertThrows(EntityNotExist.class, () -> filmController.update(film));
    }
}