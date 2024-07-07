package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.model.Film;

@Mapper
public interface FilmMapper {
    FilmMapper INSTANCE = Mappers.getMapper(FilmMapper.class);

    CreateFilmDto filmToCreateFilmDto(Film film);

    Film createFilmDtoToFilm(CreateFilmDto filmDto);

    UpdateFilmDto filmToUpdateFilmDto(Film film);

    Film updateFilmDtoToFilm(UpdateFilmDto filmDto);
}
