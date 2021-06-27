package com.marlonflorencio.demo.redis.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static java.util.UUID.randomUUID;

@Service
@AllArgsConstructor
public class TransactionService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StringRedisTemplate stringTemplate;
    private final RedisServerCommands serverCommands;

    public void execute() {

        serverCommands.flushAll();

        logger.info("====== Transaction ======");

        List<Object> txResults = stringTemplate.execute(new SessionCallback<>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {

                operations.multi();

                operations.opsForValue().set("key1", "value1");
                operations.opsForValue().set("key2", "value2");
                operations.opsForValue().set("key3", "value3");

                return operations.exec();
            }
        });

        logger.info("Result={}", txResults);

        logger.info("==============================");
    }

}
