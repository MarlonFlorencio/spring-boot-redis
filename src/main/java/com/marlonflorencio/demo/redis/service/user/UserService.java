package com.marlonflorencio.demo.redis.service.user;

import com.marlonflorencio.demo.redis.model.User;
import com.marlonflorencio.demo.redis.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;
    private final RedisServerCommands serverCommands;

    public User register(String firstName, String lastName) {

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .firstName(firstName)
                .lastName(lastName)
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

    public void peformance() {

        this.serverCommands.flushAll();

        logger.info("====== Peformance Repository ======");

        List<String> ids = new ArrayList<>();

        Instant start = Instant.now();

        for (int i = 0; i < 20_000; i++) {
            ids.add(register(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString()
            ).getId());
        }

        timeElapsed(start, "Add 20.000 items");

        logger.info("Memory={}", serverCommands.info("memory").get("used_memory_human"));

        start = Instant.now();

        ids.forEach(this::getUser);

        timeElapsed(start, "Get 20.000 items");

        logger.info("===================================");
    }

    private void timeElapsed(Instant start, String description) {
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        logger.info("{} in {} millis", description, timeElapsed.toMillis());
    }

}
