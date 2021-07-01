package com.marlonflorencio.demo.redis.service.operations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SquareService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Cacheable(value = "squareResult")
    public Double create(Double value) {
        logger.info("create square result for {}", value);
        return Math.pow(value, 2);
    }

    @CachePut(value = "squareResult")
    public Double update(Double value) {
        logger.info("update square result for {}", value);
        return Math.pow(value, 2);
    }

    @CacheEvict(value = "squareResult")
    public void delete(Double value) {
        logger.info("delte square result for {}", value);
    }

}
