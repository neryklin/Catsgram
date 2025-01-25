package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> Users(){
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user){
        if (user.getEmail()==null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (checktEmail(user.getEmail())) {
            throw new ConditionsNotMetException("Этот имейл уже используется");
        }
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(),user);
        return user;
    }

    @PutMapping
    public User updateUser (@RequestBody User user) {
        if (user.getId()==null){
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if(users.containsKey(user.getId())) {
            User oldUser  = users.get(user.getId());
            if (!user.getEmail().equals(oldUser.getEmail())) {
                if (checktEmail(user.getEmail())) {
                    throw new ConditionsNotMetException("Этот имейл уже используется");
                }
            }
            if(user.getEmail()!=null){
                oldUser.setEmail(user.getEmail());
            }
            if(user.getUsername()!=null){
                oldUser.setUsername(user.getUsername());
            }
            if(user.getPassword()!=null){
                oldUser.setPassword(user.getPassword());
            }
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id "+user.getId()+" не найден");

    }

    public boolean checktEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
