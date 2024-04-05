package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator {
    public void validate(Film film) throws ValidationException {
        if (!isNameValid(film.getName())) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (!isDescriptionValid(film.getDescription())) {
            throw new ValidationException("Длина описания фильма не должна превышать 200 символов.");
        }
        if (!isReleaseDateValid(film.getReleaseDate())) {
            throw new ValidationException("Дата релиза фильма должна быть не раньше 28 декабря 1895 года.");
        }
        if (!isDurationValid(film.getDuration())) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }

    private boolean isNameValid(String name) {
        return name != null && !name.isEmpty();
    }

    private boolean isDescriptionValid(String description) {
        return description != null && description.length() <= 200;
    }

    private boolean isReleaseDateValid(LocalDate releaseDate) {
        return releaseDate != null && !releaseDate.isBefore(LocalDate.of(1895, 12, 28));
    }

    private boolean isDurationValid(int duration) {
        return duration > 0;
    }
}