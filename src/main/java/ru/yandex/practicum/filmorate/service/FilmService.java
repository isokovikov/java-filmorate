package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film addNew(Film film);

    Film update(Film film);

    void remove(Film film);

    Film getById(Integer id);

    List<Film> getList();

    void addLike(Integer id, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    List<Film> getPopular(Integer count);

}