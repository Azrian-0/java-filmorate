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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            if (!genreStorage.checkGenresExist(film.getGenres().stream().map(Genre::getId).collect(Collectors.toList()))){
                throw new BadRequestException();
            }
        }
        return filmStorage.create(film);
    }

    public Film getById(Integer id) {
        checkFilmExist(id);
        Film film = filmStorage.getById(id);
        genreStorage.load(List.of(film));
        return film;
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
        Set<Film> films = filmStorage.getAll();
        genreStorage.load(films);
        return films;
    }

    public Film addLike(Integer filmId, Integer userId) {
        checkFilmAndUserExist(filmId, userId);
        Film film = filmStorage.addLike(filmId, userId);
        genreStorage.load(List.of(film));
        return film;
    }

    public List<Film> getPopular(Integer filmsCount) {
        List<Film> films = filmStorage.getPopular(filmsCount);
        genreStorage.load(films);
        return films;
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