package com.marlonflorencio.demo.redis.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@RedisHash("Student")
public class User implements Serializable {

    @Id
    private String id;

    private String name;

    private LocalDateTime createdAt;

}
