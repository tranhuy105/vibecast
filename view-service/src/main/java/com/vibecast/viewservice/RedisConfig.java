package com.vibecast.viewservice;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.modules(new JavaTimeModule());
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            builder.featuresToEnable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);
            builder.featuresToEnable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        };
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
//        cacheConfigurations.put("playlist", defaultCacheConfig.entryTtl(Duration.ofDays(1))); // 1 day TTL
//        cacheConfigurations.put("albumTracks", defaultCacheConfig.entryTtl(Duration.ofHours(6))); // 6 hours TTL
//        cacheConfigurations.put("artists", defaultCacheConfig.entryTtl(Duration.ofDays(1)));
//        cacheConfigurations.put("artistAlbums", defaultCacheConfig.entryTtl(Duration.ofHours(6)));
//        cacheConfigurations.put("artistTopTracks", defaultCacheConfig.entryTtl(Duration.ofHours(6)));
//        cacheConfigurations.put("tracks", defaultCacheConfig.entryTtl(Duration.ofHours(6)));
//        cacheConfigurations.put("playlists", defaultCacheConfig.entryTtl(Duration.ofHours(6)));
//        cacheConfigurations.put("playlistTracks", defaultCacheConfig.entryTtl(Duration.ofHours(6)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}

