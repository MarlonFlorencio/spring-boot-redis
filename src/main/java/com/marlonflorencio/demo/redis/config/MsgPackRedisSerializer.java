package com.marlonflorencio.demo.redis.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

class MsgPackRedisSerializer<T> implements RedisSerializer<T> {
    public static final Charset DEFAULT_CHARSET;
    private final JavaType javaType;
    private ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory())
            .registerModules(new Jdk8Module(), new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public MsgPackRedisSerializer(Class<T> type) {
        this.javaType = TypeFactory.defaultInstance().constructType(type);
    }

    public T deserialize(@Nullable byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        } else {
            try {
                return this.objectMapper.readValue(bytes, 0, bytes.length, this.javaType);
            } catch (Exception ex) {
                throw new SerializationException("Could not read MsgPack JSON: " + ex.getMessage(), ex);
            }
        }
    }

    public byte[] serialize(@Nullable Object value) throws SerializationException {
        if (value == null) {
            return new byte[0];
        } else {
            try {
                return this.objectMapper.writeValueAsBytes(value);
            } catch (Exception ex) {
                throw new SerializationException("Could not write MsgPack JSON: " + ex.getMessage(), ex);
            }
        }
    }
    
    static {
        DEFAULT_CHARSET = StandardCharsets.UTF_8;
    }

}