package ru.yandex.practicum.filmorate.service.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.EntityNotExist;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmServiceTest {

    @Qualifier("inMemoryFilmStorage")
    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private FilmService filmService;
    @Autowired
    private FilmValidator filmValidator;
    @Qualifier("inMemoryUserStorage")
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private UserService userService;
    @Autowired
    private UserValidator userValidator;

    private Film film;

    private Film film1;

    private User user;
    private User user1;

    @BeforeEach
    void setUp() {
        film = Film.builder()
                .name("First Test Film")
                .description("Test description")
                .releaseDate(LocalDate.now().minusYears(1))
                .duration(120)
                .build();
        filmService.create(film);

        film1 = Film.builder()
                .name("Second Test Film")
                .description("Test description")
                .releaseDate(LocalDate.now().minusYears(1))
                .duration(120)
                .build();
        filmService.create(film1);

        user = User.builder()
                .email("test@example.com")
                .login("TestLogin")
                .name("Test name")
                .birthday(LocalDate.now().minusYears(1))
                .build();
        userService.create(user);

        user1 = User.builder()
                .email("test1@example.com")
                .login("TestLogin")
                .name("Test name")
                .birthday(LocalDate.now().minusYears(1))
                .build();
        userService.create(user1);
    }

    @Test
    void getById() {
        Film result = filmService.getById(1);
        assertEquals(film, result);
    }

    @Test
    void deleteById() {
        filmService.deleteById(1);
        EntityNotExist exception = assertThrows(EntityNotExist.class, () -> filmService.getById(1));
        assertTrue(exception.getMessage().contains("Нет фильма с таким id."));
    }

    @Test
    void addLike() {
        Film result = filmService.addLike(1, 1);
        assertTrue(result.getLikes().contains(1));
    }

    @Test
    void deleteLike() {
        Film result = filmService.deleteLike(1, 1);
        assertFalse(result.getLikes().contains(1));
    }

    @Test
    void getPopular() {
        film.getLikes().add(1);
        film1.getLikes().add(1);
        film1.getLikes().add(2);
        filmStorage.update(film);
        filmStorage.update(film1);

        List<Film> result = filmService.getPopular(2);

        assertEquals(2, result.size());
        assertEquals(film, result.get(1));
        assertEquals(film1, result.get(0));
    }
}