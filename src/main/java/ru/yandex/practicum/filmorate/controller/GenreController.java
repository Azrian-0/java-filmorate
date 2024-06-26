package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.film.GenreService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    public Genre create(@Valid @RequestBody Genre genre) {
        log.info("Обработан POST genres запрос.");
        return genreService.create(genre);
    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable("id") Integer genreId) {
        log.info("Обработан GET genres {} запрос.", genreId);
        return genreService.getById(genreId);
    }

    @PutMapping
    public Genre update(@Valid @RequestBody Genre genre) {
        log.info("Обработан PUT genres запрос.");
        return genreService.update(genre);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer genreId) {
        log.info("Обработан DELETE genres {} запрос.", genreId);
        genreService.deleteById(genreId);
    }

    @GetMapping
    public List<Genre> getAll() {
        log.info("Обработан GET genres запрос.");
        return genreService.getAll();
    }
}