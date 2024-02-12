package ru.yandex.practicum.filmorate.storage.film;

public interface LikesStorage {
    void addLike(int filmId, long userId);

    void deleteLike(int filmId, long userId);
}