package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mpa {

    private Integer id;

    @NotBlank(message = "Название возрастного рейтинга не может быть пустым.")
    private String name;
}
