package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public interface FilmStorage {

    Film create(Film film);

    Film getById(Integer id);

    Film update(Film film);

    void deleteById(Integer id);

    Set<Film> getAll();

    Film addLike(Integer filmId, Integer userId);

    Film deleteLike(Integer filmId, Integer userId);

    List<Film> getPopular(Integer filmsCount);

    boolean isFilmExist(Integer id);
}