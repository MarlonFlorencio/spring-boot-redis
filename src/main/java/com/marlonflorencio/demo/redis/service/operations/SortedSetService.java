package com.marlonflorencio.demo.redis.service.operations;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SortedSetService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StringRedisTemplate stringTemplate;
    private final RedisServerCommands serverCommands;

    private static final String KEY_GROUP_1 = "key1";
    private static final String JOHN = "John";
    private static final String JOE = "Joe";
    private static final String DAVID = "David";
    private static final String JAMES = "James";
    private static final String JACOB = "Jacob";

    public void execute() {

        serverCommands.flushAll();

        logger.info("====== Sorted Set Operations ======");

        final ZSetOperations<String, String> operations = stringTemplate.opsForZSet();

        logger.info("Add Item={}", operations.add(KEY_GROUP_1, JOHN, 0));
        logger.info("Add Item={}", operations.add(KEY_GROUP_1, JOE, 0));
        logger.info("Add Item={}", operations.add(KEY_GROUP_1, DAVID, 0));
        logger.info("Add Item={}", operations.add(KEY_GROUP_1, JAMES, 0));
        logger.info("Add Item={}", operations.add(KEY_GROUP_1, JACOB, 0));

        this.stringTemplate.expire(KEY_GROUP_1, Duration.ofMinutes(5));

        logger.info("===");
        logger.info("Type={}", stringTemplate.type(KEY_GROUP_1));
        logger.info("TTL={}", stringTemplate.getExpire(KEY_GROUP_1));

        logger.info("===");
        logger.info("Size={}", operations.size(KEY_GROUP_1));
        logger.info("Get All(asc by default)={}", operations.range(KEY_GROUP_1, 0  ,-1));

        logger.info("===");
        logger.info("Add 20 points to Joe={}", operations.incrementScore(KEY_GROUP_1, JOE, 20));
        logger.info("Add 15 points to David={}", operations.incrementScore(KEY_GROUP_1, DAVID, 15));
        logger.info("Add 5 points to Jacob={}", operations.incrementScore(KEY_GROUP_1, JACOB, 5));
        logger.info("Remove 5 points from James={}", operations.incrementScore(KEY_GROUP_1, JAMES, -5));
        logger.info("Get All={}", formatTypedTuple(operations.rangeWithScores(KEY_GROUP_1, 0  ,-1)));

        logger.info("===");
        logger.info("All with score between 15 and 50={}", operations.reverseRangeByScore(KEY_GROUP_1, 15, 50));
        logger.info("Top 3 scores(first is greater)={}", operations.reverseRange(KEY_GROUP_1, 0, 2));
        logger.info("Top 3 scores(first is greater) with score={}", formatTypedTuple(operations.reverseRangeWithScores(KEY_GROUP_1, 0, 2)));

        logger.info("Jacob's score={}", operations.score(KEY_GROUP_1, JACOB));
        logger.info("Jacob's rank(less is better, first is 0)={}", operations.reverseRank(KEY_GROUP_1, JACOB));

        logger.info("==============================");
    }
    
    private List<String> formatTypedTuple(Set<ZSetOperations.TypedTuple<String>> items) {
        return items.stream()
                .map(item -> item.getValue().concat("=").concat(item.getScore().toString()))
                .collect(Collectors.toList());
    }

}
