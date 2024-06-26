package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbc;
    private final MpaRowMapper mapper;

    @Override
    public Mpa create(Mpa mpa) {
        String sql = "INSERT INTO ratings (name) VALUES (?)";
        jdbc.update(sql, mpa.getName());
        return mpa;
    }

    @Override
    public Mpa getById(Integer mpaId) {
        String sql = "SELECT * FROM ratings WHERE id = ?";
        return jdbc.queryForObject(sql, mapper, mpaId);
    }

    @Override
    public Mpa update(Mpa mpa) {
        String sql = "UPDATE ratings SET name = ? WHERE id = ?";
        jdbc.update(sql, mpa.getName(), mpa.getId());
        return mpa;
    }

    @Override
    public void deleteById(Integer mpaId) {
        String sql = "DELETE FROM ratings WHERE id = ?";
        jdbc.update(sql, mpaId);
    }

    @Override
    public List<Mpa> getAll() {
        String sql = "SELECT * FROM ratings ORDER BY id";
        return jdbc.query(sql, mapper);
    }

    @Override
    public Mpa getMpaByFilmId(Integer filmId) {
        String sql = "SELECT r.id, r.name FROM ratings r " +
                "JOIN films f ON r.id = f.rating_id " +
                "WHERE f.id = ?";

        return jdbc.queryForObject(sql, new Object[]{filmId}, mapper);
    }

    @Override
    public void addMpaToFilm(Film film) {
        Integer filmId = film.getId();
        Mpa mpa = film.getMpa();
        if (mpa != null) {
            String sql = "UPDATE films SET rating_id = ? WHERE id = ?";
            jdbc.update(sql, mpa.getId(), filmId);
            sql = "SELECT name FROM ratings WHERE id = ?";
            String mpaName = jdbc.queryForObject(sql, String.class, mpa.getId());
            mpa.setName(mpaName);
            film.setMpa(mpa);
        }
    }

    @Override
    public void updateMpaToFilm(Film film) {
        Integer filmId = film.getId();
        Mpa mpa = film.getMpa();
        if (mpa != null) {
            String updateSql = "UPDATE films SET rating_id = ? WHERE id = ?";
            jdbc.update(updateSql, mpa.getId(), filmId);
        }
    }

    @Override
    public boolean checkMpaExist(Integer id) {
        String sql = "SELECT COUNT(*) FROM ratings WHERE id = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}