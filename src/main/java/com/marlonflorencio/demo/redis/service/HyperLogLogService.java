package com.marlonflorencio.demo.redis.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
public class HyperLogLogService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StringRedisTemplate stringTemplate;
    private final RedisServerCommands serverCommands;

    private static final String KEY_GROUP_1 = "key1";

    public void execute() {

        serverCommands.flushAll();

        logger.info("====== HyperLogLog Operations ======");

        final HyperLogLogOperations<String, String> operations = stringTemplate.opsForHyperLogLog();

        logger.info("Add={}", operations.add(KEY_GROUP_1, "A", "A", "B", "C"));

        this.stringTemplate.expire(KEY_GROUP_1, Duration.ofMinutes(5));

        logger.info("===");
        logger.info("Type={}", stringTemplate.type(KEY_GROUP_1));
        logger.info("TTL={}", stringTemplate.getExpire(KEY_GROUP_1));
        logger.info("Size={}", operations.size(KEY_GROUP_1));

        logger.info("==============================");
    }
    
}
