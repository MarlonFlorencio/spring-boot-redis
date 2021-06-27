package com.marlonflorencio.demo.redis.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.Collections;

import static org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory;

@EnableCaching
@Configuration
public class RedisConfig {

    private final String HOSTNAME;
    private final int PORT;
    private final int DATABASE;
    private final String PASSWORD;
    private final long TIMEOUT;

    public RedisConfig(
        @Value("${redis.hostname}") String hostname,
        @Value("${redis.port}") int port,
        @Value("${redis.database}") int database,
        @Value("${redis.password}") String password,
        @Value("${redis.timeout}") long timeout
    ) {
        this.HOSTNAME = hostname;
        this.PORT = port;
        this.DATABASE = database;
        this.PASSWORD = password;
        this.TIMEOUT = timeout;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(HOSTNAME);
        config.setPort(PORT);
        config.setDatabase(DATABASE);
        config.setPassword(PASSWORD);

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ofMillis(TIMEOUT))
            .build();

        return new LettuceConnectionFactory(config, clientConfig);
    }

    @Bean
    public RedisServerCommands redisServerCommands(
            @Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory
    ) {
        return redisConnectionFactory.getConnection().serverCommands();
    }

//    @Bean
//    public RedisCacheManager cacheManager(
//            @Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory
//    ) {
//
//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(10))
//                .disableCachingNullValues();
//
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(config)
//                .withInitialCacheConfigurations(singletonMap("predefined", config))
//                .transactionAware()
//                .build();
//    }

    @Bean
    public RedisCacheManager redisCacheManager(
            @Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory
    ) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofHours(1))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
        redisCacheConfiguration.usePrefix();

        return fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(
            @Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory
    ) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        template.afterPropertiesSet();
        return template;
    }

//    @Bean
//    public RedisTemplate<String, byte[]> messagePackRedisTemplate(
//            @Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory
//    ) {
//
//        RedisTemplate<String, byte[]> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setEnableDefaultSerializer(false);
//
//        return template;
//    }

//    @Bean
//    public ObjectMapper messagePackObjectMapper() {
//        return new ObjectMapper(new MessagePackFactory())
//            .registerModule(new JavaTimeModule())
//            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//    }
}
