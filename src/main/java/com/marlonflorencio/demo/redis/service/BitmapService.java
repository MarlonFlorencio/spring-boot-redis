package com.marlonflorencio.demo.redis.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
public class BitmapService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StringRedisTemplate stringTemplate;
    private final RedisServerCommands serverCommands;

    private static final String KEY_1 = "key1";

    public void execute() {

        serverCommands.flushAll();

        logger.info("====== Bitmap Operations ======");

        final ValueOperations<String, String> operations = this.stringTemplate.opsForValue();

        operations.setBit(KEY_1, 1, true);
        operations.setBit(KEY_1, 2, false);
        operations.setBit(KEY_1, 3, true);

        logger.info("Get 1={}", operations.getBit(KEY_1, 1));
        logger.info("Get 2={}", operations.getBit(KEY_1, 2));
        logger.info("Get 3={}", operations.getBit(KEY_1, 3));

        Long bitcCount = this.stringTemplate.execute((RedisCallback<Long>) con -> con.bitCount(KEY_1.getBytes()));
        logger.info("Bitcount={}", bitcCount);

        this.stringTemplate.expire(KEY_1, Duration.ofMinutes(5));

        logger.info("===");
        logger.info("Type={}", this.stringTemplate.type(KEY_1));
        logger.info("TTL={}", this.stringTemplate.getExpire(KEY_1));
        logger.info("Size={}", operations.size(KEY_1));

        logger.info("==============================");
    }
    
}
