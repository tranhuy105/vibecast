package com.vibecast.streamservice.models;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PlaybackState {
    private boolean isPlaying;
    private String currentTrackId;
    private long position; // in milliseconds
    private long accumulatedTime; // in milliseconds
    private String deviceId;
}
