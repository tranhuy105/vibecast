package com.vibecast.viewservice.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final Logger logger = LoggerFactory.getLogger(CacheService.class);

    public void evictCacheWithPlaylistId(String playlistId) {
        String pattern = "playlists::" + playlistId + "-*";
        Set<String> keys = redisTemplate.keys(pattern);

        if (keys != null && !keys.isEmpty()) {
            logger.info("Keys found: " + keys.size());
            for (String key : keys) {
                logger.info("Deleting key: " + key);
            }
            redisTemplate.delete(keys);
            logger.info("Removed " + keys.size() + " keys for playlistId: " + playlistId);
        } else {
            logger.info("No keys found for playlistId: " + playlistId);
        }
    }
}
