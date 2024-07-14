package com.vibecast.streamservice.service;

import com.vibecast.streamservice.models.PlaybackState;
import com.vibecast.streamservice.models.ListeningHistory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final Logger logger = LoggerFactory.getLogger(PlayerService.class);
    private static final long VALID_STREAM_THRESHOLD = 30000; // 30 seconds
    private static final long SESSION_EXPIRATION = 3600; // 1 hour expiration

    public PlaybackState getPlaybackState(String userId) {
        return (PlaybackState) redisTemplate.opsForValue().get("playbackState:" + userId);
    }

    public void startOrChangeTrack(String userId, String deviceId, String trackId) {
        PlaybackState currentPlaybackState = getPlaybackState(userId);
        if (currentPlaybackState != null
                && currentPlaybackState.isPlaying()
                && !deviceId.equals(currentPlaybackState.getDeviceId())) {
            throw new IllegalStateException("Another device is currently streaming");
        }
        if (currentPlaybackState == null) {
            startPlayback(userId, deviceId, trackId);
        } else {
            processTrackSwitch(userId, trackId, currentPlaybackState);
        }
    }

    public void pausePlayback(String userId) {
        PlaybackState playbackState = getPlaybackState(userId);
        if (playbackState != null && playbackState.isPlaying()) {
            long currentTime = Instant.now().toEpochMilli();
            long listeningTime = currentTime - playbackState.getPosition();
            playbackState.setAccumulatedTime(playbackState.getAccumulatedTime() + listeningTime);

            playbackState.setPosition(currentTime);
            playbackState.setPlaying(false);
            redisTemplate.opsForValue().set("playbackState:" + userId, playbackState, SESSION_EXPIRATION, TimeUnit.SECONDS);
        }
    }

    private void startPlayback(String userId, String deviceId, String trackId) {
        PlaybackState currentPlaybackState = new PlaybackState();
        currentPlaybackState.setPlaying(true);
        currentPlaybackState.setDeviceId(deviceId);
        currentPlaybackState.setCurrentTrackId(trackId);
        currentPlaybackState.setPosition(Instant.now().toEpochMilli());
        currentPlaybackState.setAccumulatedTime(0);

        redisTemplate.opsForValue().set("playbackState:" + userId, currentPlaybackState, SESSION_EXPIRATION, TimeUnit.SECONDS);
    }

    public void resumePlayback(String userId) {
        PlaybackState playbackState = getPlaybackState(userId);
        if (playbackState != null && !playbackState.isPlaying()) {
            playbackState.setPlaying(true);
            playbackState.setPosition(Instant.now().toEpochMilli());
            redisTemplate.opsForValue().set("playbackState:" + userId, playbackState, SESSION_EXPIRATION, TimeUnit.SECONDS);
        }
    }

    private void processTrackSwitch(String userId, String newTrackId, PlaybackState playbackState) {
        if (!newTrackId.equals(playbackState.getCurrentTrackId())) {
            long listeningTime;
            if (playbackState.isPlaying()) {
                listeningTime = Instant.now().toEpochMilli() - playbackState.getPosition();
            } else {
                listeningTime = 0L;
            }
            playbackState.setAccumulatedTime(playbackState.getAccumulatedTime() + listeningTime);
            saveListeningHistoryInRedis(userId, playbackState.getCurrentTrackId(), playbackState.getAccumulatedTime());

            playbackState.setPlaying(true);
            playbackState.setCurrentTrackId(newTrackId);
            playbackState.setPosition(Instant.now().toEpochMilli());
            playbackState.setAccumulatedTime(0);

            redisTemplate.opsForValue().set("playbackState:" + userId, playbackState, SESSION_EXPIRATION, TimeUnit.SECONDS);
        }
    }

    private void saveListeningHistoryInRedis(String userId, String trackId, long listeningTime) {
        ListeningHistory history = new ListeningHistory(userId, trackId, listeningTime);
        redisTemplate.opsForList().leftPush("listeningHistory:" + userId, history);
        if (listeningTime >= VALID_STREAM_THRESHOLD) {
            // TODO: Produce an event
            logger.info("Successful stream: " + history);
        }
    }

    public List<ListeningHistory> getPlaybackHistory(String userId) {
        List<Object> historyObjects = redisTemplate.opsForList().range("listeningHistory:" + userId, 0, -1);
        List<ListeningHistory> listeningHistory = new ArrayList<>();

        if (historyObjects != null) {
            for (Object obj : historyObjects) {
                if (obj instanceof ListeningHistory) {
                    listeningHistory.add((ListeningHistory) obj);
                }
            }
        }

        return listeningHistory;
    }
}