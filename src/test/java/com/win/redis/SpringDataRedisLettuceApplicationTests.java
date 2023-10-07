package com.win.redis;

import com.redis.testcontainers.RedisContainer;
import com.win.service.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
@RequiredArgsConstructor
public class SpringDataRedisLettuceApplicationTests {

    private final RedisCacheService redisCacheService;

    @Container
    private static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(6279);

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry){
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6279).toString());
        registry.add("default.redis.connection", () -> "standalone");
    }

    String key;
    String value;
    String hashKey;

    @BeforeEach
    public  void setUp(){
        key = "Name";
        value = "Java";
        hashKey = "Subject";
    }

    @Test
    void testSetup(){
        assertTrue(REDIS_CONTAINER.isRunning());
    }

    @ParameterizedTest
    @Test
    void testValueOps(){
        redisCacheService.putSimple(key, value);
        String retrievedValue = redisCacheService.getSimple(key);
        assertEquals(value, retrievedValue);
    }
}
