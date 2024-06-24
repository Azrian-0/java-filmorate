package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private Integer id;

    private Set<Integer> likes = new HashSet<>();
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;

    @NotBlank(message = "Описание фильма не может быть пустым.")
    @Size(max = 200, message = "Длина описания фильма не должна превышать 200 символов.")
    private String description;

    @NotNull(message = "Дата релиза не может быть пустой.")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной.")
    private int duration;

    @AssertTrue(message = "Дата релиза фильма должна быть не раньше 28 декабря 1895 года.")
    @JsonIgnore
    public boolean isReleaseDateValid() {
        return releaseDate != null && !releaseDate.isBefore(LocalDate.of(1895, 12, 28));
    }

    private LinkedHashSet<Genre> genres;

    private Mpa mpa;
}