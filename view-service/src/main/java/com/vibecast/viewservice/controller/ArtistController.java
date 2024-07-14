package com.vibecast.viewservice.controller;

import com.vibecast.viewservice.model.persistance.Album;
import com.vibecast.viewservice.model.persistance.Artist;
import com.vibecast.viewservice.model.response.PlaylistWithTracksResponseDto;
import com.vibecast.viewservice.service.ArtistService;
import com.vibecast.viewservice.service.MockDataService;
import com.vibecast.viewservice.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artists")
public class ArtistController {
    private final ArtistService artistService;
    private final MockDataService mockDataService;

    @GetMapping
    public ResponseEntity<List<Artist>> getAllArtists() {
        List<Artist> artists = artistService.getAllArtists();
        return ResponseEntity.ok(artists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtist(@PathVariable String id) throws Exception {
        Artist artist = artistService.getArtistById(id);
        return ResponseEntity.ok(artist);
    }

    @GetMapping("/{id}/albums")
    public ResponseEntity<List<Album>> getArtistAlbums(@PathVariable String id) {
        List<Album> albums = artistService.getAlbumsByArtistId(id);
        return ResponseEntity.ok(albums);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewArtistProfile() {}

    @PutMapping("/{id}/profile-image")
    public void updateArtistProfileImage(@PathVariable String id) {}

    @PutMapping("/{id}/bio-image")
    public void updateArtistBioImage(@PathVariable String id){}

}
