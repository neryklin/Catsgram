package ru.yandex.practicum.catsgram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Component
@RequiredArgsConstructor
public class PostService {
    private final UserService userService;
    private final Map<Long, Post> posts = new HashMap<>();

    public Collection<Post> findAll(Long size, String sort, Long from) {
        if (sort.equals("asc")) {
            return posts.entrySet().stream()
                    .sorted((o1, o2) -> o1.getValue().getPostDate().compareTo(o2.getValue().getPostDate()))
                    .skip(from)
                    .limit(size)
                    .map(o-> o.getValue())
                    .collect(Collectors.toSet());



        } else if (sort.equals("desc")) {
            return posts.entrySet().stream()
                    .sorted((o1, o2) -> o2.getValue().getPostDate().compareTo(o1.getValue().getPostDate()))
                    .skip(from)
                    .limit(size)
                    .map(o-> o.getValue())
                    .collect(Collectors.toSet());
        }
        return posts.values();
    }

    public Optional<Post> findById(long postId) {
        return posts.values().stream()
                .filter(o -> o.getId()==postId)
                .findFirst();
    }


    public Post update(Post newPost) {
        // проверяем необходимые условия
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }


    public Post create(Post post) {
        if (userService.findUserById(post.getAuthorId()).isEmpty()) {
            throw new ConditionsNotMetException("Автор с id = "+post.getAuthorId()+ " не найден");
        }
        // проверяем выполнение необходимых условий
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        // формируем дополнительные данные
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        // сохраняем новую публикацию в памяти приложения
        posts.put(post.getId(), post);
        return post;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}