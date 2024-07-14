package com.vibecast.viewservice.controller;

import com.vibecast.viewservice.model.response.PlaylistSummary;
import com.vibecast.viewservice.service.MockDataService;
import com.vibecast.viewservice.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/me")
@RestController
@RequiredArgsConstructor
public class LibraryController {
    private final PlaylistService playlistService;
    private final MockDataService mockDataService;

    @GetMapping("/test")
    public void test() {
//        mockDataService.generateMockData();
    }

    @GetMapping("/playlists")
    public ResponseEntity<List<PlaylistSummary>> getMyPlaylists(){
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/saved-playlists")
    public void getMySavedPlaylists() {}

    @PutMapping("/saved-playlists")
    public void addItemToMySavedPlaylist() {}

    @GetMapping("/saved-albums")
    public void getMySavedAlbums() {}

    @PutMapping("/saved-albums")
    public void addItemToMySavedAlbums() {}

    @GetMapping("/following")
    public void getFollowedArtist() {}

    @PostMapping("/following")
    public void followingArtist() {}

    @GetMapping("/collections")
    public void getMyCollections() {}
}
