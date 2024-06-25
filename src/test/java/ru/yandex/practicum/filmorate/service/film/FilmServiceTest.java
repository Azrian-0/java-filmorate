package ru.yandex.practicum.filmorate.service.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmServiceTest {

    private Film film;
    private Film film1;
    private User user;

    @Autowired
    private FilmService filmService;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
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

        film1 = Film.builder()
                .name("Second Test Film")
                .description("Test description")
                .releaseDate(LocalDate.now().minusYears(1))
                .duration(120)
                .build();
        film1.setGenres(new LinkedHashSet<>());
        film1.setLikes(new LinkedHashSet<>());
        film1.setMpa(Mpa.builder()
                .id(1)
                .name("G")
                .build());

        user = User.builder()
                .email("test@example1.com")
                .login("TestLogin1")
                .name("Test name1")
                .birthday(LocalDate.now().minusYears(1))
                .build();
    }

    @Test
    void create() {
        filmService.create(film);
        Film createdFilm = filmService.getById(film.getId());
        assertNotNull(createdFilm);
        assertEquals(film.getId(), createdFilm.getId());
    }

    @Test
    void getById() {
        filmService.create(film);
        Film createdFilm = filmService.getById(film.getId());
        assertNotNull(createdFilm);
        assertEquals(film.getId(), createdFilm.getId());
    }

    @Test
    void update() {
        filmService.create(film);
        film.setName("New name");
        filmService.update(film);
        Film updatedFilm = filmService.getById(film.getId());
        assertNotNull(updatedFilm);
        assertEquals(film.getName(), updatedFilm.getName());
    }

    @Test
    void deleteById() {
        filmService.create(film);
        int filmId = film.getId();
        filmService.deleteById(filmId);
        Executable executable = () -> filmService.getById(filmId);
        assertThrows(EntityNotFoundException.class, executable);
    }

    @Test
    void getAll() {
        filmService.create(film);
        filmService.create(film1);
        Set<Film> films = filmService.getAll();
        assertNotNull(films);
        assertFalse(films.isEmpty());
        assertTrue(films.contains(film));
        assertTrue(films.contains(film1));
    }

    @Test
    void getPopular() {
        filmService.create(film);
        filmService.create(film1);
        userService.create(user);
        filmService.addLike(film.getId(), user.getId());
        List<Film> films = filmService.getPopular(2);
        assertNotNull(films);
        assertEquals(2, films.size());
    }
}