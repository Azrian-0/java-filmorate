package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.GenreService;
import ru.yandex.practicum.filmorate.service.film.MpaService;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;

import java.util.*;

@Primary
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbc;
    private final FilmRowMapper mapper;
    private final MpaService mpaService;
    private final GenreService genreService;
    private final LikeDbStorage likeDbStorage;

    private Map<String, Object> toMap(Film film) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", film.getName());
        parameters.put("description", film.getDescription());
        parameters.put("release_date", film.getReleaseDate());
        parameters.put("duration", film.getDuration());
        parameters.put("rating_id", film.getMpa().getId());
        return parameters;
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbc)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        Number id = insertActor.executeAndReturnKey(toMap(film));

        film.setId(id.intValue());
        genreService.addGenresToFilm(film);
        genreService.addGenreNamesToFilm(film);
        mpaService.addMpaToFilm(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";
        jdbc.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        return film;
    }

    @Override
    public Set<Film> getAll() {
        String sql = "SELECT * FROM films ORDER BY id";
        return new LinkedHashSet<>(jdbc.query(sql, mapper));
    }

    @Override
    public Film getById(Integer id) {
        String sql = "SELECT * FROM films WHERE id = ?";
        return jdbc.queryForObject(sql, mapper, id);
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM films WHERE id = ?";
        jdbc.update(sql, id);
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        Film film = getById(filmId);
        String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES(?, ?)";
        jdbc.update(sqlQuery, filmId, userId);
        return film;
    }

    @Override
    public Film deleteLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbc.update(sql, filmId, userId);
        return getById(filmId);
    }

    @Override
    public List<Film> getPopular(Integer filmsCount) {
        String sql = "SELECT f.*, COUNT(l.user_id) AS likes " +
                "FROM films f " +
                "LEFT JOIN likes l ON f.id = l.film_id " +
                "GROUP BY f.id " +
                "ORDER BY likes DESC " +
                "LIMIT ?";
        return jdbc.query(sql, mapper, filmsCount);
    }

    @Override
    public boolean isFilmExist(Integer id) {
        String sql = "SELECT COUNT(*) FROM films WHERE id = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}