package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExist;
import ru.yandex.practicum.filmorate.exception.EntityNotExist;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmValidator filmValidator = new FilmValidator();
    private final Set<Film> films = new LinkedHashSet<>();

    @GetMapping
    public Set<Film> findAll() {
        return films;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (films.contains(film)) {
            throw new EntityAlreadyExist("Такой фильм уже существует.");
        }
        filmValidator.validate(film);
        film.setId(Film.incrementId());
        films.add(film);
        log.info("Фильм {} добавлен.", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (!films.contains(film)) {
            throw new EntityNotExist("Такого фильма не существует.");
        }
        filmValidator.validate(film);
        films.remove(film);
        films.add(film);
        log.info("Фильм {} обновлен.", film);
        return film;
    }
}