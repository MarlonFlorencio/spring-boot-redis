package com.marlonflorencio.demo.redis.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
public class StringService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StringRedisTemplate stringTemplate;
    private final RedisServerCommands serverCommands;

    private static final String KEY_1 = "key1";
    private static final String KEY_2 = "key2";
    private static final String KEY_3 = "key3";

    public void execute() {

        serverCommands.flushAll();

        logger.info("====== String Operations ======");

        final ValueOperations<String, String> operations = this.stringTemplate.opsForValue();

        operations.set(KEY_1, "ABC", Duration.ofMinutes(5));

        logger.info("Get={}", operations.get(KEY_1));
        logger.info("Type={}", this.stringTemplate.type(KEY_1));
        logger.info("TTL={}", this.stringTemplate.getExpire(KEY_1));

        logger.info("===");
        operations.append(KEY_1, "123");
        logger.info("Append={}", operations.get(KEY_1));
        logger.info("Size={}", operations.size(KEY_1));

        logger.info("===");
        logger.info("Increment Int={}", operations.increment(KEY_2, 1));
        logger.info("Increment Int={}", operations.increment(KEY_2, 1));
        logger.info("Increment Double={}", operations.increment(KEY_3, 1.23123));
        logger.info("Increment Double={}", operations.increment(KEY_3, 1.53123));

        logger.info("==============================");

    }

}
