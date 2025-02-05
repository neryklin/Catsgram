package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class PostController {
    private final PostService postService;
//    http://127.0.0.1:8080/posts
//    http://127.0.0.1:8080/posts?sort=desc&size=128
//    http://127.0.0.1:8080/posts?sort=asc&size=5&from=10


    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public Collection<Post> findAll(
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(defaultValue = "5") Long from
    ) {
        return postService.findAll(size,sort,from);
    }

    @GetMapping("/posts/{postId}")
    public Optional<Post> findById(@PathVariable int postId) {
        return postService.findById(postId);
    }

    @PostMapping("/posts")
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping("/posts")
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}