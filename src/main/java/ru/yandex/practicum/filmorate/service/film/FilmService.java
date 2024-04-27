package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotExist;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;


import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    static final int DEFAULT_POPULAR_COUNT = 10;

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;


    public Film create(Film film) {

        return filmStorage.create(film);
    }

    public Film update(Film film) {

        return filmStorage.update(film);
    }

    public Set<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(Integer id) {
        return filmStorage.getById(id);
    }

    public void deleteById(Integer id) {
        filmStorage.deleteById(id);
    }

    public Film addLike(Integer filmId, Integer userId) {
        if (!userStorage.isUserExist(userId)) {
            throw new EntityNotExist("Нет пользователя с таким id.");
        }
        Film film = filmStorage.getById(filmId);
        film.getLikes().add(userId);
        log.info("Добавлен лайк фильму: {} от пользователя c id: {}.", film.getName(), userId);
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        if (!userStorage.isUserExist(userId)) {
            throw new EntityNotExist("Нет пользователя с таким id.");
        }
        Film film = filmStorage.getById(filmId);
        film.getLikes().remove(userId);
        log.info("Удален лайк фильму: {} от пользователя c id: {}.", film.getName(), userId);
        return film;
    }

    public List<Film> getPopular(int filmsCount) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(filmsCount > 0 ? filmsCount : DEFAULT_POPULAR_COUNT)
                .collect(Collectors.toList());
    }
}