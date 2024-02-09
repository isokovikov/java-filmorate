package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    List<Mpa> findAll();

    Optional<Mpa> getMpaByID(int id);
}
