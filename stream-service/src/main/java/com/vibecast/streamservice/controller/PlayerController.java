package com.vibecast.streamservice.controller;

import com.vibecast.streamservice.feign.ViewServiceClient;
import com.vibecast.streamservice.feign.model.Track;
import com.vibecast.streamservice.models.ListeningHistory;
import com.vibecast.streamservice.models.PlaybackState;
import com.vibecast.streamservice.models.PlaybackStateResponse;
import com.vibecast.streamservice.service.PlayerService;
import com.vibecast.streamservice.service.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/me/player")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;
    private final S3Service s3Service;
    private final ViewServiceClient viewServiceClient;

    private static final String USER_ID = "1"; // Assume userId is always 1 for testing

    @GetMapping("/recent")
    public ResponseEntity<List<ListeningHistory>> getPlaybackHistory() {
        return ResponseEntity.ok(playerService.getPlaybackHistory(USER_ID));
    }

    @GetMapping
    public ResponseEntity<PlaybackStateResponse> getPlaybackState() {
        PlaybackState playbackState = playerService.getPlaybackState(USER_ID);
        if (playbackState == null) {
            return ResponseEntity.ok(null);
        }
        PlaybackStateResponse playbackStateResponse = new PlaybackStateResponse(
                playbackState.isPlaying(),
                viewServiceClient.getTrackById(playbackState.getCurrentTrackId()),
                playbackState.getPosition(),
                playbackState.getAccumulatedTime(),
                playbackState.getDeviceId()
        );
        return ResponseEntity.ok(playbackStateResponse);
    }

    @PutMapping("/pause")
    public ResponseEntity<Void> pausePlayback() {
        playerService.pausePlayback(USER_ID);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/resume")
    public ResponseEntity<Void> resumePlayback() {
        playerService.resumePlayback(USER_ID);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/play")
    public ResponseEntity<String> startPlayback(HttpServletRequest request, @RequestParam String trackId) throws FileNotFoundException {
        String deviceId = request.getHeader("User-Agent");
        String presignedUrl = s3Service.generatePresignedUrl(trackId);
        playerService.startOrChangeTrack(USER_ID, deviceId, trackId);
        return ResponseEntity.ok(presignedUrl);
    }
}

