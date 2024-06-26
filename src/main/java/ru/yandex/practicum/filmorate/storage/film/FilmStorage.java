package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Set;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    Set<Film> getAll();

    Film getById(Integer id);

    void deleteById(Integer id);
}