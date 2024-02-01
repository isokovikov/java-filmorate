package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.intellij.lang.annotations.Identifier;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class User {
    @Generated
    @Getter // только геттер, сеттер не нужен
    private Integer id;
    @Email
    @NotNull
    private String email;
    @NotBlank
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
}
