package com.win.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private RedisTemplate<Object, Object> redisTemplate;

    public RedisCacheService(@Qualifier("customRedisTemplate") RedisTemplate<Object, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    public void putSimple(Object key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void putSimpleWithTTL(Object key, Object value, long ttl){
        redisTemplate.opsForValue().set(key,value);
        redisTemplate.expire(key, Duration.ofSeconds(ttl));
    }

    public <T> T getSimple(Object key) {
        return (T)redisTemplate.opsForValue().get(key);
    }

    public void put(Object key, Object hashKey, Object value, long ttl){
        redisTemplate.opsForHash().put(key,hashKey, value);
        redisTemplate.expire(hashKey, Duration.ofSeconds(ttl));
    }

    public <T> T get(Object key, Object hashKey){
        return (T) redisTemplate.opsForHash().get(key, hashKey);
    }

    public long delete(Object key, Object hashKey){
        return redisTemplate.opsForHash().delete(key, hashKey);
    }

    public void putWithTTL(Object key, Object hashKey, Object value, long ttl){
        redisTemplate.opsForHash().put(key, hashKey, value);
        redisTemplate.expire(key, Duration.ofSeconds(ttl));
    }

    public void expire(Object key, long ttl){
        redisTemplate.expire(key, Duration.ofSeconds(ttl));
    }
}
