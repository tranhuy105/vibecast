package com.vibecast.streamservice.util;

public class RedisKeyUtil {

    private static final String PLAYBACK_STATE_PREFIX = "playbackState:";
    private static final String LISTENING_HISTORY_PREFIX = "listeningHistory:";
    private static final String LOCK_PLAYBACK_STATE_PREFIX = "lock:playbackState:";

    public static String getPlaybackStateKey(String userId) {
        return PLAYBACK_STATE_PREFIX + userId;
    }

    public static String getListeningHistoryKey(String userId) {
        return LISTENING_HISTORY_PREFIX + userId;
    }

    public static String getLockPlaybackStateKey(String userId) {
        return LOCK_PLAYBACK_STATE_PREFIX + userId;
    }
}

