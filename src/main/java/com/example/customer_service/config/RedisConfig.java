package com.example.customer_service.config;

import com.example.customer_service.dto.CustomerResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Configuration
public class RedisConfig {

    @Bean
    public CacheManager cacheManager(
            RedisConnectionFactory connectionFactory,
            ObjectMapper objectMapper,
            @Value("${spring.cache.redis.time-to-live:10m}") Duration ttl) {

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(ttl)
                .disableCachingNullValues();

        RedisCacheConfiguration customerConfig = jsonConfig(
                defaultConfig,
                new JacksonJsonRedisSerializer<>(objectMapper, CustomerResponse.class));

        RedisCacheConfiguration customerListConfig = jsonConfig(
                defaultConfig,
                new JacksonJsonRedisSerializer<>(
                        objectMapper,
                        objectMapper.getTypeFactory().constructCollectionType(
                                List.class,
                                CustomerResponse.class)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(Map.of(
                        "customer", customerConfig,
                        "customerByEmail", customerConfig,
                        "customerList", customerListConfig))
                .build();
    }

    private <T> RedisCacheConfiguration jsonConfig(
            RedisCacheConfiguration config,
            JacksonJsonRedisSerializer<T> serializer) {

        return config.serializeValuesWith(
                RedisSerializationContext.SerializationPair
                        .fromSerializer(serializer));
    }
}
