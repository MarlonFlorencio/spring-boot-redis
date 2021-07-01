package com.marlonflorencio.demo.redis.service.operations;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static org.springframework.data.redis.connection.RedisGeoCommands.DistanceUnit.KILOMETERS;
import static org.springframework.data.redis.connection.RedisGeoCommands.DistanceUnit.METERS;
import static org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs;

@Service
@AllArgsConstructor
public class GeoHashService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StringRedisTemplate stringTemplate;
    private final RedisServerCommands serverCommands;

    private static final String KEY_1 = "key1";
    private static final String EMPIRE_STATE = "empire-state";
    private static final String WALL_STREET = "wall-street";
    private static final String TIMES_SQUARE = "times-square";
    private static final String CENTRAL_PARK = "central-park";

    public void execute() {

        serverCommands.flushAll();

        logger.info("====== Geo Hash Operations ======");

        final GeoOperations<String, String> operations = this.stringTemplate.opsForGeo();

        logger.info("Add location={}", operations.add(KEY_1, new GeoLocation<>(CENTRAL_PARK, new Point(40.765915, -73.976085))));
        logger.info("Add location={}", operations.add(KEY_1, new GeoLocation<>(TIMES_SQUARE, new Point(40.758286, -73.985531))));
        logger.info("Add location={}", operations.add(KEY_1, new GeoLocation<>(EMPIRE_STATE, new Point(40.748073, -73.984805))));
        logger.info("Add location={}", operations.add(KEY_1, new GeoLocation<>(WALL_STREET, new Point(40.7068046,-74.0097714))));

        logger.info("===");
        logger.info("Distance between empire-state and wall-street={}", operations.distance(KEY_1, EMPIRE_STATE, WALL_STREET, KILOMETERS ));
        logger.info("Distance between empire-state and central-park={}", operations.distance(KEY_1, EMPIRE_STATE, CENTRAL_PARK, KILOMETERS ));

        logger.info("===");
        logger.info("Places until 2km far from empire-state");
        operations.radius(
                KEY_1,
                EMPIRE_STATE,
                new Distance(2, KILOMETERS),
                newGeoRadiusArgs().sortAscending().includeDistance()
        ).forEach(item -> logger.info("{}, distance: {}", item.getContent().getName(), item.getDistance()));

        logger.info("==============================");
    }
    
}
