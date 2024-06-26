package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.film.MpaService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;

    @PostMapping
    public Mpa create(@Valid @RequestBody Mpa mpa) {
        log.info("Обработан POST mpa запрос.");
        return mpaService.create(mpa);
    }

    @GetMapping("/{id}")
    public Mpa getById(@PathVariable("id") Integer mpaId) {
        log.info("Обработан GET mpa {} запрос.", mpaId);
        return mpaService.getById(mpaId);
    }

    @PutMapping
    public Mpa update(@Valid @RequestBody Mpa mpa) {
        log.info("Обработан PUT mpa запрос.");
        return mpaService.update(mpa);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer mpaId) {
        log.info("Обработан DELETE mpa {} запрос.", mpaId);
        mpaService.deleteById(mpaId);
    }

    @GetMapping
    public List<Mpa> getAll() {
        log.info("Обработан GET mpa запрос.");
        return mpaService.getAll();
    }
}