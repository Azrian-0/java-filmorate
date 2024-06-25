package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public interface GenreStorage {

    Genre create(Genre genre);

    Genre getById(Integer genreId);

    Genre update(Genre genre);

    void deleteById(Integer genreId);

    List<Genre> getAll();

    LinkedHashSet<Genre> getGenresByFilmId(Integer filmId);

    void addGenresToFilm(Film film);

    void updateGenresToFilm(Film film);

    boolean checkGenreExist(Integer id);

    void addGenreNamesToFilm(Film film);

    boolean checkGenresExist(List<Integer> ids);

    void load(Collection<Film> films);
}