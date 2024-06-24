package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExist;
import ru.yandex.practicum.filmorate.exception.EntityNotExist;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

    @Qualifier("inMemoryUserStorage")
    @Autowired
    private final UserStorage userStorage;

    private final Set<Film> films = new HashSet<>();

    private int nextId = 1;

    private int incrementId() {
        return nextId++;
    }

    static final int DEFAULT_POPULAR_COUNT = 10;

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

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        if (!userStorage.isUserExist(userId)) {
            throw new EntityNotExist("Нет пользователя с таким id.");
        }
        Film film = getById(filmId);
        film.getLikes().add(userId);
        log.info("Добавлен лайк фильму: {} от пользователя c id: {}.", film.getName(), userId);
        return film;
    }

    @Override
    public Film deleteLike(Integer filmId, Integer userId) {
        if (!userStorage.isUserExist(userId)) {
            throw new EntityNotExist("Нет пользователя с таким id.");
        }
        Film film = getById(filmId);
        film.getLikes().remove(userId);
        log.info("Удален лайк фильму: {} от пользователя c id: {}.", film.getName(), userId);
        return film;
    }

    @Override
    public List<Film> getPopular(Integer filmsCount) {
        return films.stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(filmsCount > 0 ? filmsCount : DEFAULT_POPULAR_COUNT)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFilmExist(Integer userId) {
        return films.stream().anyMatch(film -> film.getId().equals(userId));
    }
}