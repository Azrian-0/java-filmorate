package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbc;

    @Override
    public List<Like> getLikesByFilmId(Integer filmId) {
        String sql = "SELECT * FROM likes WHERE film_id = ?";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(Like.class), filmId);
    }

    @Override
    public List<Like> getLikesByUserId(Integer userId) {
        String sql = "SELECT * FROM likes WHERE user_id = ?";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(Like.class), userId);
    }
}
