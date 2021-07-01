package com.marlonflorencio.demo.redis.service.user;

import com.marlonflorencio.demo.redis.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.marlonflorencio.demo.redis.config.RedisSerializationBuilder.getMsgPackRedisTemplate;

@Service
public class UserMsgPackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RedisServerCommands serverCommands;
    private final RedisTemplate<String, User> redisTemplate;
    private final ValueOperations<String, User> operations;

    public UserMsgPackService(
            LettuceConnectionFactory redisConnectionFactory,
            RedisServerCommands serverCommands
    ) {
        this.redisTemplate = getMsgPackRedisTemplate(redisConnectionFactory, User.class);
        this.operations =  this.redisTemplate.opsForValue();
        this.serverCommands =  serverCommands;
    }

    public User register(String firstName) {

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .build();

        operations.set(user.getId(), user, Duration.ofHours(1));

        return user;
    }

    public Optional<User> getUser(String id) {
        return Optional.ofNullable(operations.get(id));
    }

    public void peformance() {

        serverCommands.flushAll();

        logger.info("======= Peformance MsgPack ========");

        List<String> ids = new ArrayList<>();

        Instant start = Instant.now();

        for (int i = 0; i < 20_000; i++) {
            ids.add(
                    register(UUID.randomUUID().toString()).getId()
            );
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
