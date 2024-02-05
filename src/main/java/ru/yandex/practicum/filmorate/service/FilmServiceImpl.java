package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film addNew(Film film) {
        return filmStorage.addNew(film);
    }

    @Override
    public Film update(Film film) {
        if (filmStorage.getById(film.getId()).isPresent()) {
            return filmStorage.update(film);
        } else {
            throw new FilmNotFoundException("The film was not found");
        }
    }

    @Override
    public Film remove(Integer id) {
        if (filmStorage.getById(id).isPresent()) {
            return filmStorage.remove(id);
        } else {
            throw new FilmNotFoundException("The film was not found");
        }
    }

    @Override
    public Film getById(Integer id) {
        return filmStorage.getById(id).orElseThrow(() ->
                new FilmNotFoundException("The film was not found"));
    }

    @Override
    public List<Film> getList() {
        return filmStorage.getList();
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        // Проверяем наличие фильма
        Film film = filmStorage.getById(filmId)
                .orElseThrow(() -> new FilmNotFoundException("Film with ID " + filmId + " was not found"));

        // Проверяем наличие пользователя
        userStorage.getById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " was not found"));

        // Добавляем лайк к фильму и обновляем его
        film.getLikes().add(userId);
        return filmStorage.update(film);
    }

    @Override
    public Film removeLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getById(filmId)
                .orElseThrow(() -> new FilmNotFoundException("Film with ID " + filmId + " was not found"));

        userStorage.getById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " was not found"));

        film.getLikes().remove(userId);
        return filmStorage.update(film);
    }

    @Override
    public List<Film> getPopular(Integer count) {
        return filmStorage.getList()
                .stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}