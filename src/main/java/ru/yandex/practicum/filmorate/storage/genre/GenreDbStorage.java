package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbc;
    private final GenreRowMapper mapper;


    @Override
    public Genre create(Genre genre) {
        String sql = "INSERT INTO genres (name) VALUES (?)";
        jdbc.update(sql, genre.getName());
        return genre;
    }

    @Override
    public Genre getById(Integer genreId) {
        String sql = "SELECT * FROM genres WHERE id = ? ORDER BY id";
        return jdbc.queryForObject(sql, mapper, genreId);
    }

    @Override
    public Genre update(Genre genre) {
        String sql = "UPDATE genres SET name = ? WHERE id = ?";
        jdbc.update(sql, genre.getName(), genre.getId());
        return genre;
    }

    @Override
    public void deleteById(Integer genreId) {
        String sql = "DELETE FROM genres WHERE id = ?";
        jdbc.update(sql, genreId);
    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT * FROM genres ORDER BY id";
        return jdbc.query(sql, mapper);
    }

    @Override
    public LinkedHashSet<Genre> getGenresByFilmId(Integer filmId) {
        String sql = "SELECT g.* FROM genres g " +
                "JOIN films_genres fg ON g.id = fg.genre_id " +
                "WHERE fg.film_id = ? ORDER BY id";
        return new LinkedHashSet<>(jdbc.query(sql, mapper, filmId));
    }

    @Override
    public void addGenresToFilm(Film film) {
        Integer filmId = film.getId();
        LinkedHashSet<Genre> genreSet = film.getGenres();
        if (genreSet != null && !genreSet.isEmpty()) {
            for (Genre genre : genreSet) {
                String mergeSql = "MERGE INTO films_genres (film_id, genre_id) KEY(film_id, genre_id) VALUES (?, ?)";
                jdbc.update(mergeSql, filmId, genre.getId());
            }
        }
    }

    @Override
    public void updateGenresToFilm(Film film) {
        Integer filmId = film.getId();
        String deleteSql = "DELETE FROM films_genres WHERE film_id = ?";
        jdbc.update(deleteSql, filmId);
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            addGenresToFilm(film);
        }
    }

    @Override
    public boolean checkGenreExist(Integer id) {
        String sql = "SELECT COUNT(*) FROM genres WHERE id = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public void addGenreNamesToFilm(Film film) {
        Integer filmId = film.getId();
        String sql = "SELECT g.id, g.name FROM genres g " +
                "JOIN films_genres fg ON g.id = fg.genre_id " +
                "WHERE fg.film_id = ?";

        List<Genre> genreList = jdbc.query(sql, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("name"));
            return genre;
        }, filmId);
        LinkedHashSet<Genre> genres = new LinkedHashSet<>(genreList);
        film.setGenres(genres);
    }
}