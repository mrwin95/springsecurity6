package com.win.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService<T> {
    private final RedisTemplate<String, T> redisTemplate;

    public T getCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    public void setCache(String key, T value){
        redisTemplate.opsForValue().set(key, value);
    }

}
