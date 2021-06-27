package com.marlonflorencio.demo.redis.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
public class ListService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StringRedisTemplate stringTemplate;
    private final RedisServerCommands serverCommands;

    private static final String KEY = "key";

    public void execute() {

        serverCommands.flushAll();

        logger.info("====== List Operations ======");

        final ListOperations<String, String> operations = stringTemplate.opsForList();

        logger.info("Push Head={}", operations.leftPush(KEY, "A"));
        logger.info("Push Head={}", operations.leftPush(KEY, "B"));
        logger.info("Push Head={}", operations.leftPush(KEY, "C"));
        logger.info("Push Tail={}", operations.rightPush(KEY, "D"));

        this.stringTemplate.expire(KEY, Duration.ofMinutes(5));

        logger.info("===");
        logger.info("Type={}", stringTemplate.type(KEY));
        logger.info("TTL={}", stringTemplate.getExpire(KEY));
        logger.info("Size={}", operations.size(KEY));

        logger.info("===");
        logger.info("Get 0={}", operations.index(KEY, 0));
        logger.info("Get All={}", operations.range(KEY, 0, -1));

        logger.info("===");
        operations.trim(KEY, 0,2);
        logger.info("Trim(0, 2)");
        logger.info("Get All after Trim={}", operations.range(KEY, 0, -1));

        logger.info("===");
        logger.info("Pop left={}", operations.leftPop(KEY));
        logger.info("Get All after pop left={}", operations.range(KEY, 0, -1));

        logger.info("===");
        logger.info("Pop right={}", operations.rightPop(KEY));
        logger.info("Get All after pop right={}", operations.range(KEY, 0, -1));

        logger.info("==============================");
    }

}
