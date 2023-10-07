package com.win.config;

import com.win.utils.CustomRedisSerializer;
import io.lettuce.core.ReadFrom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RedisConfig implements CachingConfigurer {

    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private Integer redisPort;

    @Value("${default.redis.connection:standalone}")
    private String connectionType;

    @Value("${default.redis.TTL:64}")
    private int timeToLive;
    private final GenericObjectPoolConfig genericObjectPoolConfig;
    private final RedisProperties redisProperties;

    @Bean
    public LettucePoolingClientConfiguration poolingClientConfiguration(){
        return LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig).build();
    }
    @Bean(name = "customConnectionFactory")
    public LettuceConnectionFactory connectionFactory(LettucePoolingClientConfiguration clientConfiguration){
        if(connectionType.equalsIgnoreCase("standalone")){
            return new LettuceConnectionFactory(redisStandaloneConfiguration(), clientConfiguration);
        }else {
            return new LettuceConnectionFactory(redisSentinelConfiguration(), clientConfiguration);
        }
    }

    @Bean
    @Override
    public RedisCacheManager cacheManager(){
        return RedisCacheManager
                .builder(this.connectionFactory(poolingClientConfiguration()))
                .cacheDefaults(this.cacheConfiguration())
                .build();
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration(){
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(timeToLive))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new CustomRedisSerializer()));
    }

    private RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        if(StringUtils.hasText(redisProperties.getPassword())){
            configuration.setPassword(redisProperties.getPassword());
        }
        return configuration;
    }

    private RedisSentinelConfiguration redisSentinelConfiguration() {
        RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration()
                .master(redisProperties.getSentinel().getMaster());

        redisProperties.getSentinel().getNodes().forEach(s ->
                sentinelConfiguration.sentinel(s.split(":")[0],
                        Integer.valueOf(s.split(":")[1])));
        if(StringUtils.hasText(redisProperties.getPassword())){
            sentinelConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        }

        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
                .commandTimeout(redisProperties.getTimeout())
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();

        return sentinelConfiguration;
    }

    @Bean(name = "customRedisTemplate")
    public RedisTemplate<Object, Object> customRedisTemplate(@Qualifier("customConnectionFactory") RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setDefaultSerializer(new CustomRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new CustomRedisSerializer());
        redisTemplate.setHashValueSerializer(new CustomRedisSerializer());
        return  redisTemplate;
    }

//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory){
//        // crete json
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
//        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//
//        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
//
//        RedisSerializationContext.SerializationPair jsonSerializer = RedisSerializationContext.SerializationPair.fromSerializer(jsonRedisSerializer);
//
//     return RedisCacheManager.builder().cacheDefaults().serializeValuesWith(jsonSerializer).create(connectionFactory);
//    }
}
