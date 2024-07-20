package com.vibecast.streamservice.service;

import com.vibecast.streamservice.models.PlaybackState;
import com.vibecast.streamservice.models.ListeningHistory;
import com.vibecast.streamservice.util.RedisKeyUtil;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
    private final RedissonClient redissonClient;
    private final Logger logger = LoggerFactory.getLogger(PlayerService.class);
    private static final long VALID_STREAM_THRESHOLD = 30000; // 30 seconds
    private static final long SESSION_EXPIRATION = 3600; // 1 hour expiration

    public PlaybackState getPlaybackState(String userId) {
        return (PlaybackState) redisTemplate.opsForValue().get(RedisKeyUtil.getPlaybackStateKey(userId));
    }

    public void startOrChangeTrack(String userId, String deviceId, String trackId) {
        RLock lock = redissonClient.getLock(RedisKeyUtil.getLockPlaybackStateKey(userId));
        lock.lock();
        try {
            PlaybackState currentPlaybackState = getPlaybackState(userId);
            validatePlaybackState(currentPlaybackState, deviceId);
            if (currentPlaybackState == null) {
                startPlayback(userId, deviceId, trackId);
            } else {
                processTrackSwitch(userId, trackId, currentPlaybackState);
            }
        } finally {
            lock.unlock();
        }
    }

    public void pausePlayback(String userId) {
        RLock lock = redissonClient.getLock(RedisKeyUtil.getLockPlaybackStateKey(userId));
        lock.lock();
        try {
            PlaybackState playbackState = getPlaybackState(userId);
            if (isPlaying(playbackState)) {
                updatePlaybackStateOnPause(playbackState);
                savePlaybackState(userId, playbackState);
            }
        } finally {
            lock.unlock();
        }
    }

    public void resumePlayback(String userId) {
        RLock lock = redissonClient.getLock(RedisKeyUtil.getLockPlaybackStateKey(userId));
        lock.lock();
        try {
            PlaybackState playbackState = getPlaybackState(userId);
            if (isPaused(playbackState)) {
                updatePlaybackStateOnResume(playbackState);
                savePlaybackState(userId, playbackState);
            }
        } finally {
            lock.unlock();
        }
    }

    private void processTrackSwitch(String userId, String newTrackId, PlaybackState playbackState) {
        if (!newTrackId.equals(playbackState.getCurrentTrackId())) {
            updateListeningHistory(userId, playbackState);
            resetPlaybackStateForNewTrack(playbackState, newTrackId);
            savePlaybackState(userId, playbackState);
        }
    }

    public List<ListeningHistory> getPlaybackHistory(String userId) {
        List<Object> historyObjects = redisTemplate.opsForList().range(RedisKeyUtil.getListeningHistoryKey(userId), 0, -1);
        return convertToListeningHistoryList(historyObjects);
    }

    private void validatePlaybackState(PlaybackState playbackState, String deviceId) {
        if (playbackState != null && playbackState.isPlaying() && !deviceId.equals(playbackState.getDeviceId())) {
            throw new IllegalStateException("Another device is currently streaming");
        }
    }

    private boolean isPlaying(PlaybackState playbackState) {
        return playbackState != null && playbackState.isPlaying();
    }

    private boolean isPaused(PlaybackState playbackState) {
        return playbackState != null && !playbackState.isPlaying();
    }

    private void updatePlaybackStateOnPause(PlaybackState playbackState) {
        long currentTime = Instant.now().toEpochMilli();
        long listeningTime = currentTime - playbackState.getPosition();
        playbackState.setAccumulatedTime(playbackState.getAccumulatedTime() + listeningTime);
        playbackState.setPosition(currentTime);
        playbackState.setPlaying(false);
    }

    private void updatePlaybackStateOnResume(PlaybackState playbackState) {
        playbackState.setPlaying(true);
        playbackState.setPosition(Instant.now().toEpochMilli());
    }

    private void savePlaybackState(String userId, PlaybackState playbackState) {
        redisTemplate.opsForValue().set(RedisKeyUtil.getPlaybackStateKey(userId), playbackState, SESSION_EXPIRATION, TimeUnit.SECONDS);
    }

    private void startPlayback(String userId, String deviceId, String trackId) {
        PlaybackState currentPlaybackState = createNewPlaybackState(deviceId, trackId);
        savePlaybackState(userId, currentPlaybackState);
    }

    private PlaybackState createNewPlaybackState(String deviceId, String trackId) {
        PlaybackState playbackState = new PlaybackState();
        playbackState.setPlaying(true);
        playbackState.setDeviceId(deviceId);
        playbackState.setCurrentTrackId(trackId);
        playbackState.setPosition(Instant.now().toEpochMilli());
        playbackState.setAccumulatedTime(0);
        return playbackState;
    }

    private void resetPlaybackStateForNewTrack(PlaybackState playbackState, String newTrackId) {
        playbackState.setPlaying(true);
        playbackState.setCurrentTrackId(newTrackId);
        playbackState.setPosition(Instant.now().toEpochMilli());
        playbackState.setAccumulatedTime(0);
    }

    private void updateListeningHistory(String userId, PlaybackState playbackState) {
        long listeningTime = playbackState.getAccumulatedTime() + calculateListeningTime(playbackState);
        saveListeningHistoryInRedis(userId, playbackState.getCurrentTrackId(), listeningTime);
        playbackState.setAccumulatedTime(0); // Reset accumulated time after saving history
    }

    private long calculateListeningTime(PlaybackState playbackState) {
        if (playbackState.isPlaying()) {
            return Instant.now().toEpochMilli() - playbackState.getPosition();
        }
        return 0L;
    }

    private void saveListeningHistoryInRedis(String userId, String trackId, long listeningTime) {
        ListeningHistory history = new ListeningHistory(userId, trackId, listeningTime);
        redisTemplate.opsForList().leftPush(RedisKeyUtil.getListeningHistoryKey(userId), history);
        if (listeningTime >= VALID_STREAM_THRESHOLD) {
            // TODO: Produce an event
            logger.info("Successful stream: " + history);
        }
    }

    private List<ListeningHistory> convertToListeningHistoryList(List<Object> historyObjects) {
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
