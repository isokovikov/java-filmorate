package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private final LocalDate earlyDate = LocalDate.of(1895, 12, 28);

    @GetMapping
    public List<Film> returnAll() {
        log.info("Request GET /films received");
        return filmService.getList();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Integer id) {
        log.info("Request GET /films/{} received", id);
        return filmService.getById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") @Positive Integer count) {
        log.info("Request GET /films/popular received");
        return filmService.getPopular(count);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addNew(@Valid @RequestBody Film film) {
        filmValidation(film);
        log.info("Request POST /films received");
        Film addFilm = filmService.addNew(film);
        log.info("A new film " + film.getName() + " has been added, " + film.getId());
        return addFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        filmValidation(film);
        log.info("Request PUT /films received");
        Film updateFilm = filmService.update(film);
        log.info("Film with ID " + film.getId() + " was updated");
        return updateFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public boolean addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Request PUT /films/{}/like/{} received", id, userId);
        filmService.addLike(id, userId);
        log.info("Like added!");
        return true;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Integer id) {
        log.info("Request DELETE /users/{}", id);
        Film removeFilm = filmService.getById(id);
        filmService.remove(removeFilm);
        log.info("User was deleted");
    }

    @DeleteMapping("/{id}/like/{userId}")
    public boolean removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Request DELETE /films/{}/like/{} received", id, userId);
        filmService.removeLike(id, userId);
        log.info("FLike was deleted");
        return true;

    }

    private void filmValidation(Film film) {
        if (film == null) {
            throw new ValidationException("Film cannot be null");
        }

        if (film.getReleaseDate() == null) {
            throw new ValidationException("Release date cannot be null");
        }

        if (film.getReleaseDate().isBefore(earlyDate)) {
            log.info("The film failed validation: release date is too early");
            throw new ValidationException("Release date — no earlier than December 28, 1895");
        }
    }
}
