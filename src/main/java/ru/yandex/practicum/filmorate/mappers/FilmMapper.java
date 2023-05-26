package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class FilmMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        long filmId = rs.getLong("id");
        String filmName = rs.getString("films.name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        Mpa mpa = new Mpa(rs.getLong("rating_id"), rs.getString("rating_mpa.name"));
        Film film = new Film(filmName, description, releaseDate, duration);
        film.setId(filmId);
        film.setMpa(mpa);
        return film;
    }
}
