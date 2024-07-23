package com.vibecast.viewservice.controller;

import com.vibecast.viewservice.model.response.PlaylistSummary;
import com.vibecast.viewservice.model.response.PlaylistWithTracksResponseDto;
import com.vibecast.viewservice.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;

    @PutMapping("/playlists/{playlistId}")
    public void changePlaylistDetail(@PathVariable String playlistId) {}

    @PutMapping("/playlists/{playlistId}/followers")
    public void followPlaylist(@PathVariable String  playlistId){}

    @DeleteMapping("/playlists/{playlistId}/followers")
    public void unfollowPlaylist(@PathVariable String playlistId) {}


    @GetMapping("/playlists/{playlistId}")
    public ResponseEntity<PlaylistWithTracksResponseDto> getItemFromPlaylist(@PathVariable String playlistId,
                                                                             @RequestParam int page) {
        if (page < 1) {
            throw new RuntimeException("Page must be >= 1");
        }
        int pageSize = 200;
        return ResponseEntity.ok(playlistService.findPlaylistById(playlistId, page, pageSize));
    }

    @PostMapping("/playlists/{playlistId}/tracks")
    public ResponseEntity<String> addItemToPlaylist(@PathVariable String playlistId,
                                  @RequestParam List<String> ids) {
        playlistService.addTracksToPlaylist(playlistId, ids);
        return ResponseEntity.status(201).body("Success");
    }

    @DeleteMapping("/playlists/{playlistId}/tracks")
    public ResponseEntity<String> removeItemFromPlaylist(@PathVariable String playlistId,
                                       @RequestParam List<String> ids) {
        playlistService.removeTracksFromPlaylist(playlistId, ids);
        return ResponseEntity.ok("Success");
    }

    @PutMapping ("/playlists/{playlistId}/tracks")
    public void updateItemInPlaylist(@PathVariable String playlistId,
                                     @RequestParam Integer rangeStart,
                                     @RequestParam Integer insertBefore,
                                     @RequestParam Integer rangeLength) {
//            playlistService.reorderTracksInPlaylist(playlistId, rangeStart, insertBefore, rangeLength);
    }

    @GetMapping("/user/{userId}/playlists")
    public ResponseEntity<List<PlaylistSummary>> getUserPlaylist(@PathVariable String userId) {
        return ResponseEntity.ok(playlistService.findByOwnerId(userId));
    }

    @PostMapping("/user/{userId}/playlists")
    public void createPlaylist(@PathVariable String userId){}

}
