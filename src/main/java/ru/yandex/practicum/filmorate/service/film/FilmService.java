package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    public Film create(Film film) {
        if (film.getMpa() != null && !mpaStorage.checkMpaExist(film.getMpa().getId())) {
            throw new BadRequestException();
        }
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            Set<Integer> genreIds = new HashSet<>();
            for (Genre genre : film.getGenres()) {
                if (!genreStorage.checkGenreExist(genre.getId())) {
                    throw new BadRequestException();
                }
                if (!genreIds.add(genre.getId())) {
                    throw new BadRequestException();
                }
            }
        }
        return filmStorage.create(film);
    }

    public Film getById(Integer id) {
        checkFilmExist(id);
        return filmStorage.getById(id);
    }

    public Film update(Film film) {
        checkFilmExist(film.getId());
        return filmStorage.update(film);
    }

    public void deleteById(Integer id) {
        checkFilmExist(id);
        filmStorage.deleteById(id);
    }

    public Set<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film addLike(Integer filmId, Integer userId) {
        checkFilmAndUserExist(filmId, userId);
        return filmStorage.addLike(filmId, userId);
    }

    public List<Film> getPopular(Integer filmsCount) {
        return filmStorage.getPopular(filmsCount);
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        checkFilmAndUserExist(filmId, userId);
        return filmStorage.deleteLike(filmId, userId);
    }

    private void checkFilmExist(Integer id) {
        if (!filmStorage.isFilmExist(id)) {
            throw new EntityNotFoundException();
        }
    }

    private void checkFilmAndUserExist(Integer filmId, Integer userId) {
        if (!filmStorage.isFilmExist(filmId) || !(userStorage.isUserExist(userId))) {
            throw new EntityNotFoundException();
        }
    }
}