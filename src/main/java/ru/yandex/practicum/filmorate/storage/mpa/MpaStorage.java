package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

public interface MpaStorage {

    Mpa create(Mpa mpa);

    Mpa getById(Integer mpaId);

    Mpa update(Mpa mpa);

    void deleteById(Integer mpaId);

    List<Mpa> getAll();

    Mpa getMpaByFilmId(Integer filmId);

    void addMpaToFilm(Film film);

    void updateMpaToFilm(Film film);

    boolean checkMpaExist(Integer id);
}
