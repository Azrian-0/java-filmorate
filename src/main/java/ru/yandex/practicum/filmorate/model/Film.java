package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@Builder
public class Film {
    private static int nextId = 0;

    private Integer id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    private LocalDate releaseDate;

    @Positive
    private int duration;

    public static Integer incrementId() {
        nextId++;
        return nextId;
    }
}