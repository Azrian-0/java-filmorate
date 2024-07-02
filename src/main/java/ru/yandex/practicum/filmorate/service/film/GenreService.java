package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public Genre create(Genre genre) {
        return genreStorage.create(genre);
    }

    public Genre getById(int genreId) {
        checkGenreExist(genreId);
        return genreStorage.getById(genreId);
    }

    public Genre update(Genre genre) {
        checkGenreExist(genre.getId());
        return genreStorage.update(genre);
    }

    public void deleteById(Integer genreId) {
        checkGenreExist(genreId);
        genreStorage.deleteById(genreId);
    }

    public List<Genre> getAll() {
        return genreStorage.getAll();
    }

    private void checkGenreExist(Integer id) {
        if (!genreStorage.checkGenreExist(id)) {
            throw new EntityNotFoundException();
        }
    }

    public void addGenresToFilm(Film film) {
        genreStorage.addGenresToFilm(film);
    }

    public void addGenreNamesToFilm(Film film) {
        genreStorage.addGenreNamesToFilm(film);
    }

    public LinkedHashSet<Genre> getGenresByFilmId(Integer filmId) {
        return genreStorage.getGenresByFilmId(filmId);
    }

    public void updateGenresToFilm(Film film) {
        genreStorage.updateGenresToFilm(film);
    }
}