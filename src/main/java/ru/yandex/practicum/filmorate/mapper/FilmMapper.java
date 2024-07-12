package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.model.Film;

@Mapper
public interface FilmMapper {
    FilmMapper INSTANCE = Mappers.getMapper(FilmMapper.class);

    Film createFilmDtoToFilm(CreateFilmDto filmDto);

    Film updateFilmDtoToFilm(UpdateFilmDto filmDto);

    Film filmDtoToFilm(FilmDto filmDto);

    FilmDto filmToFilmDto(Film film);

    CreateFilmDto filmToCreateFilmDto(Film film);

    UpdateFilmDto filmToUpdateFilmDto(Film film);
}
