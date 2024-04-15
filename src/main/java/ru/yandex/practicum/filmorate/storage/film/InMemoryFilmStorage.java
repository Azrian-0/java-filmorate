package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExist;
import ru.yandex.practicum.filmorate.exception.EntityNotExist;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Set<Film> films = new HashSet<>();
    private int nextId = 1;

    private int incrementId() {
        return nextId++;
    }

    @Override
    public Film create(Film film) {
        if (films.contains(film)) {
            throw new EntityAlreadyExist("Такой фильм уже существует.");
        }
        film.setId(incrementId());
        films.add(film);
        log.info("Фильм {} добавлен.", film.getName());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.contains(film)) {
            throw new EntityNotExist("Такого фильма не существует.");
        }
        films.add(film);
        log.info("Фильм {} обновлен.", film);
        return film;
    }

    @Override
    public Set<Film> getAll() {
        return films;
    }

    @Override
    public Film getById(Integer id) {
        return films.stream()
                .filter(film -> film.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotExist("Нет фильма с таким id."));
    }

    @Override
    public void deleteById(Integer id) {
        Film filmToRemove = getById(id);
        films.remove(filmToRemove);
        log.info("Фильм {} удален.", filmToRemove.getName());
    }
}