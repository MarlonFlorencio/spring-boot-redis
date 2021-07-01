package com.marlonflorencio.demo.redis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
@RedisHash(value = "Student", timeToLive = 3600)
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @Id
    private String id;

    private String firstName;

    private LocalDateTime createdAt;

}
