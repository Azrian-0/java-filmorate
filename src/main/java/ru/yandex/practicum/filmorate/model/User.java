package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@Builder
public class User {
    private static int nextId = 1;

    private Integer id;

    @NotBlank(message = "Электронная почта не может быть пустой.")
    @Email(message = "Неверный формат электронной почты.")
    private String email;

    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы.")
    private String login;

    private String name;

    @NotNull(message = "Дата рождения не может быть пустой.")
    @Past(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;

    public static Integer incrementId() {
        return nextId++;
    }
}