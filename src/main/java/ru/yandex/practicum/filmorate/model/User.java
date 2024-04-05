package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@Builder
public class User {
    private static int nextId = 0;

    private Integer id;
    @NotBlank
    @Email(message = "Неверный формат электронной почты")
    private String email;

    @NotBlank
    private String login;

    private String name;

    @NotNull
    @Past
    private LocalDate birthday;

    public static Integer incrementId() {
        nextId++;
        return nextId;
    }
}
