package com.vibecast.streamservice.models;

import com.vibecast.streamservice.feign.model.Track;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PlaybackStateResponse {
    private boolean isPlaying;
    private Track currentTrack;
    private long position; // in milliseconds
    private long accumulatedTime; // in milliseconds
    private String deviceId;
}