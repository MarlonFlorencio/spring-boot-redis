package com.marlonflorencio.demo.redis.service;

import com.marlonflorencio.demo.redis.model.User;
import com.marlonflorencio.demo.redis.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User register(String username) {

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .name(username)
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    public Optional<User> getUser(String id) {
        return userRepository.findById(id);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

}
