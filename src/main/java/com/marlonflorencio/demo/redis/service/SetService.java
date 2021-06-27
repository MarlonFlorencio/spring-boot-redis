package com.marlonflorencio.demo.redis.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
public class SetService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StringRedisTemplate stringTemplate;
    private final RedisServerCommands serverCommands;

    private static final String KEY_GROUP_1 = "key1";
    private static final String KEY_GROUP_2 = "key2";
    private static final String KEY_GROUP_3 = "key2";
    private static final String JOE = "Joe";
    private static final String DAVID = "David";
    private static final String JOHN = "John";
    private static final String JAMES = "James";
    private static final String JACOB = "Jacob";
    private static final String KYLE = "Kyle";
    private static final String JOSEPH = "Joseph";

    public void execute() {

        serverCommands.flushAll();

        logger.info("====== Set Operations ======");

        final SetOperations<String, String> operations = stringTemplate.opsForSet();

        logger.info("Create Group 1={}", operations.add(KEY_GROUP_1, JOE, DAVID, JOHN, JAMES, JACOB));
        logger.info("Create Group 2={}", operations.add(KEY_GROUP_2, KYLE, DAVID, JOSEPH, JAMES));

        this.stringTemplate.expire(KEY_GROUP_1, Duration.ofMinutes(5));

        logger.info("===");
        logger.info("Type={}", stringTemplate.type(KEY_GROUP_1));
        logger.info("TTL={}", stringTemplate.getExpire(KEY_GROUP_1));

        logger.info("===");
        logger.info("Get All items from Group 1={}", operations.members(KEY_GROUP_1));
        logger.info("Get All items from Group 2={}", operations.members(KEY_GROUP_2));

        logger.info("===");
        logger.info("is Joe member of Group 1={}", operations.isMember(KEY_GROUP_1, JOE));
        logger.info("is Joe member of Group 2={}", operations.isMember(KEY_GROUP_2, JOE));

        logger.info("===");
        logger.info("Union between Group 1 and Group 2={}", operations.union(KEY_GROUP_1, KEY_GROUP_2));
        logger.info("Intersect between Group 1 and Group 2={}", operations.intersect(KEY_GROUP_1, KEY_GROUP_2));
        logger.info("Difference between Group 1 and Group 2={}", operations.difference(KEY_GROUP_1, KEY_GROUP_2));
        logger.info("Difference between Group 2 and Group 1={}", operations.difference(KEY_GROUP_2, KEY_GROUP_1));

        logger.info("===");
        logger.info("Intersect between Group 1 and Group 2 and Store in Group 3={}", operations.intersectAndStore(KEY_GROUP_1, KEY_GROUP_2, KEY_GROUP_3));
        logger.info("Get All items from Group 3={}", operations.members(KEY_GROUP_3));

        logger.info("===");
        logger.info("Move Jacob to Group 2={}", operations.move(KEY_GROUP_1, JACOB, KEY_GROUP_2));
        logger.info("Get All items from Group 1={}", operations.members(KEY_GROUP_1));
        logger.info("Get All items from Group 2={}", operations.members(KEY_GROUP_2));

        logger.info("===");
        logger.info("Remove Jacob from Group 2={}", operations.remove(KEY_GROUP_2, JACOB));
        logger.info("Get All items from Group 2={}", operations.members(KEY_GROUP_2));

        logger.info("==============================");
    }

}
