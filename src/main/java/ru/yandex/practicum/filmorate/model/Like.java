package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Like {

    private Integer id;

    @NonNull
    private Integer userId;

    @NonNull
    private Integer filmId;
}
