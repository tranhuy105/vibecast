package com.vibecast.viewservice.service;

import com.vibecast.viewservice.model.response.PlaylistSummary;
import com.vibecast.viewservice.model.response.PlaylistWithTracksResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final Logger logger = LoggerFactory.getLogger(CacheService.class);

    public void evictAllPlaylistCache(String playlistId) {
        String pattern = "playlist::" + playlistId + "::page:*";

        Set<String> keys = redisTemplate.keys(pattern);

        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            logger.debug("Removed " + keys.size() + " keys for playlistId: {}", playlistId);
        } else {
            logger.debug("No cache entries found for playlistId: {}", playlistId);
        }
    }

    public void incrementPlaylistSaveCount(String playlistId, long incrementBy) {
        redisTemplate.opsForValue().increment("playlist:saveCount:" + playlistId, incrementBy);
    }

    public Long getPlaylistSaveCount(String playlistId) {
        String key = "playlist:saveCount:" + playlistId;
        Object value = redisTemplate.opsForValue().get(key);

        if (value instanceof String countStr) {
            try {
                return Long.valueOf(countStr);
            } catch (NumberFormatException e) {
                logger.error("Invalid number format in Redis for key: " + key, e);
                return 0L;
            }
        }

        return 0L;
    }

    public void removePlaylistSaveCount(String playlistId) {
        redisTemplate.delete("playlist:saveCount:" + playlistId);
    }

    public Set<String> getAllPlaylistSavedCountKeys() {
        return redisTemplate.keys("playlist:savedCount:*");
    }
}
