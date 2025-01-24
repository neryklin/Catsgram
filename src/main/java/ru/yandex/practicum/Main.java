package ru.yandex.practicum;

import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
//        System.out.println("Hello world!");
//        User user = new User(1L,"sdfsdf","dfdf#@dfdf","sdfsdf", Instant.now());
//        System.out.println(user);
//
//        User usr2 = user.toBuilder().password("sdfg").build();
        SpringApplication.run(Main.class,args);
    }
}