package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreStorage genreStorage;

    @Override
    public List<Genre> findAll() {
        return genreStorage.findAll();
    }

    @Override
    public Genre getGenreById(int id) {
        return genreStorage.getGenreById(id).orElseThrow(() -> new GenreNotFoundException("Genre with " + id + " not found."));
    }
}
