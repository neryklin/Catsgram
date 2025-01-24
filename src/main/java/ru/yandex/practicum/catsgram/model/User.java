package ru.yandex.practicum.catsgram.model;

import lombok.*;

import java.time.Instant;


@Data
@EqualsAndHashCode(of ={"email"})
@ToString(of ={"id"})
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    Long id;
    String username;
    String email;
    String password;
    Instant registrationDate;
}
