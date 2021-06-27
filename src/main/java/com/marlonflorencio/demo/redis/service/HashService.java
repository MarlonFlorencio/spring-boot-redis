package com.marlonflorencio.demo.redis.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class HashService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StringRedisTemplate stringTemplate;
    private final RedisServerCommands serverCommands;

    private static final String KEY = "key";
    private static final String HASH_KEY_1 = "hk1";
    private static final String HASH_KEY_2 = "hk2";
    private static final String HASH_KEY_3 = "hk3";
    private static final String HASH_KEY_4 = "hk4";

    public void execute() {

        serverCommands.flushAll();

        logger.info("====== Hash Operations ======");

        final HashOperations<String, String, String> operations = stringTemplate.opsForHash();

        operations.put(KEY, HASH_KEY_1, "A");

        Map<String, String> values = new HashMap<>();
        values.put(HASH_KEY_2, "B");
        values.put(HASH_KEY_3, "C");
        operations.putAll(KEY, values);

        this.stringTemplate.expire(KEY, Duration.ofMinutes(5));

        logger.info("Type={}", stringTemplate.type(KEY));
        logger.info("TTL={}", stringTemplate.getExpire(KEY));

        logger.info("===");
        logger.info("Get HASH_KEY_1={}", operations.get(KEY, HASH_KEY_1));
        logger.info("Get size HASH_KEY_1={}", operations.lengthOfValue(KEY, HASH_KEY_1));

        logger.info("===");
        logger.info("Has HASH_KEY_1={}", operations.hasKey(KEY, HASH_KEY_1));
        logger.info("Has XPTO={}", operations.hasKey(KEY, "XPTO"));

        logger.info("===");
        logger.info("Increment Int={}", operations.increment(KEY, HASH_KEY_4 , 10));
        logger.info("Increment Int={}", operations.increment(KEY, HASH_KEY_4 , 20));

        logger.info("===");
        logger.info("Keys count={}", operations.size(KEY));
        logger.info("Entries={}", operations.entries(KEY));
        logger.info("Keys={}", operations.keys(KEY));
        logger.info("Values={}", operations.values(KEY));

        logger.info("==============================");
    }

}
