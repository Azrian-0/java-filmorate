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
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GenreServiceTest {

    @Autowired
    private FilmService filmService;
    @Autowired
    private GenreService genreService;


    private Film film;
    private Genre genre;
    private Genre genre1;


    @BeforeEach
    void setUp() {
        genre = Genre.builder()
                .id(7)
                .name("Фантастика")
                .build();

        genre1 = Genre.builder()
                .id(8)
                .name("Мюзикл")
                .build();

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
    void create() {
        genreService.create(genre);
        Genre createdGenre = genreService.getById(genre.getId());
        assertNotNull(createdGenre);
        assertEquals(genre.getId(), createdGenre.getId());
    }

    @Test
    void getById() {
        genreService.create(genre);
        Genre createdGenre = genreService.getById(genre.getId());
        assertNotNull(createdGenre);
        assertEquals(genre.getId(), createdGenre.getId());
    }

    @Test
    void update() {
        genreService.create(genre);
        genre.setName("New name");
        genreService.update(genre);
        Genre updatedGenre = genreService.getById(genre.getId());
        assertNotNull(updatedGenre);
        assertEquals(genre.getName(), updatedGenre.getName());
    }

    @Test
    void deleteById() {
        genreService.create(genre);
        int genreId = genre.getId();
        genreService.deleteById(genreId);
        Executable executable = () -> genreService.getById(genreId);
        assertThrows(EntityNotFoundException.class, executable);
    }

    @Test
    void getAll() {
        genreService.create(genre);
        genreService.create(genre1);
        List<Genre> genres = genreService.getAll();
        assertNotNull(genres);
        assertFalse(genres.isEmpty());
        assertTrue(genres.contains(genre));
        assertTrue(genres.contains(genre1));
    }

    @Test
    void addGenresToFilm() {
        filmService.create(film);
        genreService.create(genre);
        film.setGenres(new LinkedHashSet<>(Collections.singletonList(genre)));
        genreService.addGenresToFilm(film);
        LinkedHashSet<Genre> genres = genreService.getGenresByFilmId(film.getId());
        assertEquals(1, genres.size());
        assertTrue(genres.contains(genre));
    }

    @Test
    void getGenresByFilmId() {
        filmService.create(film);
        genreService.create(genre);
        genreService.create(genre1);
        film.setGenres(new LinkedHashSet<>(Arrays.asList(genre, genre1)));
        genreService.addGenresToFilm(film);
        LinkedHashSet<Genre> genres = genreService.getGenresByFilmId(film.getId());
        assertEquals(2, genres.size());
        assertTrue(genres.contains(genre) && genres.contains(genre1));
    }

    @Test
    void updateGenresToFilm() {
        filmService.create(film);
        genreService.create(genre);
        film.setGenres(new LinkedHashSet<>(Collections.singletonList(genre)));
        genreService.addGenresToFilm(film);
        genreService.create(genre1);
        film.setGenres(new LinkedHashSet<>(Collections.singletonList(genre1)));
        genreService.updateGenresToFilm(film);
        LinkedHashSet<Genre> genres = genreService.getGenresByFilmId(film.getId());
        assertEquals(1, genres.size());
        assertTrue(genres.contains(genre1));
    }
}