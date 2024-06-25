package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Set;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Set<Film> getAll() {
        log.info("Обработан GET films запрос.");
        return filmService.getAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Обработан POST film запрос.");
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обработан PUT film запрос.");
        return filmService.update(film);
    }

    @GetMapping("/{filmId}")
    public Film getById(@PathVariable Integer filmId) {
        log.info("Обработан GET film {} запрос.", filmId);
        return filmService.getById(filmId);
    }

    @DeleteMapping("/{filmId}")
    public void delete(@PathVariable Integer filmId) {
        log.info("Обработан DELETE film {} запрос.", filmId);
        filmService.deleteById(filmId);
    }


    @PutMapping("/{filmId}/like/{userId}")
    public Film addLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.info("Обработан PUT film {} like запрос.", filmId);
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.info("Обработан DELETE film {} like запрос.", filmId);
        return filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@Positive @RequestParam(name = "count", defaultValue = "0") int filmsCount) {
        log.info("Обработан GET top {} film  запрос.", filmsCount);
        return filmService.getPopular(filmsCount);
    }
}