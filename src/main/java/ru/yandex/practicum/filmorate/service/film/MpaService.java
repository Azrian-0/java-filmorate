package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Mpa create(Mpa mpa) {
        return mpaStorage.create(mpa);
    }

    public Mpa getById(Integer mpaId) {
        checkMpaExist(mpaId);
        return mpaStorage.getById(mpaId);
    }

    public Mpa update(Mpa mpa) {
        checkMpaExist(mpa.getId());
        return mpaStorage.update(mpa);
    }

    public void deleteById(Integer mpaId) {
        checkMpaExist(mpaId);
        mpaStorage.deleteById(mpaId);
    }

    public List<Mpa> getAll() {
        return mpaStorage.getAll();
    }

    private void checkMpaExist(Integer id) {
        if (!mpaStorage.checkMpaExist(id)) {
            throw new EntityNotFoundException();
        }
    }

    public void addMpaToFilm(Film film) {
        mpaStorage.addMpaToFilm(film);
    }

    public Mpa getMpaByFilmId(Integer filmId) {
        return mpaStorage.getMpaByFilmId(filmId);
    }

    public void updateMpaToFilm(Film film) {
        mpaStorage.updateMpaToFilm(film);
    }
}