package ru.yandex.practicum.filmorate.service.film;

import org.junit.jupiter.api.Assertions;
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

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MpaServiceTest {

    private Film film;
    private Mpa mpa;
    private Mpa mpa1;

    @Autowired
    private MpaService mpaService;
    @Autowired
    private FilmService filmService;

    @BeforeEach
    void setUp() {
        mpa = Mpa.builder()
                .id(6)
                .name("0+")
                .build();

        mpa1 = Mpa.builder()
                .id(7)
                .name("6+")
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
        mpaService.create(mpa);
        Mpa createdMpa = mpaService.getById(mpa.getId());
        assertNotNull(createdMpa);
        assertEquals(mpa.getId(), createdMpa.getId());
    }

    @Test
    void getById() {
        mpaService.create(mpa);
        Mpa createdMpa = mpaService.getById(mpa.getId());
        assertNotNull(createdMpa);
        assertEquals(mpa.getId(), createdMpa.getId());
    }

    @Test
    void update() {
        mpaService.create(mpa);
        mpa.setName("New name");
        mpaService.update(mpa);
        Mpa updatedMpa = mpaService.getById(mpa.getId());
        assertNotNull(updatedMpa);
        assertEquals(mpa.getName(), updatedMpa.getName());
    }

    @Test
    void deleteById() {
        mpaService.create(mpa);
        int mpaId = mpa.getId();
        mpaService.deleteById(mpaId);
        Executable executable = () -> mpaService.getById(mpaId);
        assertThrows(EntityNotFoundException.class, executable);
    }

    @Test
    void getAll() {
        mpaService.create(mpa);
        mpaService.create(mpa1);
        List<Mpa> mpas = mpaService.getAll();
        assertNotNull(mpas);
        assertFalse(mpas.isEmpty());
        assertTrue(mpas.contains(mpa));
        assertTrue(mpas.contains(mpa1));
    }

    @Test
    void addMpaToFilm() {
        filmService.create(film);
        mpaService.create(mpa);
        film.setMpa(mpa);
        mpaService.addMpaToFilm(film);
        Film foundFilm = filmService.getById(film.getId());
        Assertions.assertNotNull(foundFilm.getMpa());
        Assertions.assertEquals(mpa.getName(), foundFilm.getMpa().getName());
    }

    @Test
    void getMpaByFilmId() {
        filmService.create(film);
        Mpa foundMpa = mpaService.getMpaByFilmId(film.getId());
        Assertions.assertNotNull(foundMpa);
        Assertions.assertEquals(mpaService.getAll().get(0).getName(), foundMpa.getName());
    }

    @Test
    void updateMpaToFilm() {
        filmService.create(film);
        mpaService.create(mpa);
        film.setMpa(mpa);
        mpaService.updateMpaToFilm(film);
        Film updatedFilm = filmService.getById(film.getId());
        Assertions.assertNotNull(updatedFilm.getMpa());
        Assertions.assertEquals(mpa.getName(), updatedFilm.getMpa().getName());
    }
}