package com.marlonflorencio.demo.redis.service;

import com.marlonflorencio.demo.redis.config.TestRedisConfiguration;
import com.marlonflorencio.demo.redis.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = TestRedisConfiguration.class)
public class UserServiceIntegrationTest {

    private static final String NAME = "name";

    @Autowired
    private UserService userService;

    @Test
    public void shouldSaveUser_toRedis() {
        final User saved = this.userService.register(NAME);

        assertTrue(isNotBlank(saved.getId()));
        assertEquals(NAME, saved.getName());
        assertNotNull(saved.getCreatedAt());
    }
}