package com.marlonflorencio.demo.redis.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

import static java.util.UUID.randomUUID;

@Service
@AllArgsConstructor
public class PipelineService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StringRedisTemplate stringTemplate;
    private final RedisServerCommands serverCommands;

    public void execute() {

        serverCommands.flushAll();

        logger.info("====== Pipeline ======");

        Instant start = Instant.now();
        this.stringTemplate.executePipelined((RedisCallback<Boolean>) con -> {
                    StringRedisConnection stringRedisConn = (StringRedisConnection) con;
                    for (int i = 0; i < 100000; i++) {
                        stringRedisConn.set(randomUUID().toString(), "a");
                    }
                    return null;
                }
        );
        timeElapsed(start, "Add 100.000 items using pipeline");

        //
        start = Instant.now();
        ValueOperations<String, String> operations = this.stringTemplate.opsForValue();
        for (int i = 0; i < 100000; i++) {
            operations.set(randomUUID().toString(), "a");
        }
        timeElapsed(start, "Add 100.000 items");

        logger.info("==============================");
    }

    private void timeElapsed(Instant start, String description) {
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        logger.info("{} in {} millis", description, timeElapsed.toMillis());
    }
}
