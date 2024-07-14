package com.vibecast.viewservice.controller;

import com.vibecast.viewservice.model.persistance.Album;
import com.vibecast.viewservice.model.persistance.Track;
import com.vibecast.viewservice.model.response.PaginatedObjectDto;
import com.vibecast.viewservice.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albums")
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;

    @GetMapping
    public ResponseEntity<PaginatedObjectDto<Album>> getSeveralAlbums(@RequestParam int page,
                                                               @RequestParam int pageSize) {
        return ResponseEntity.ok(albumService.findAll(page, pageSize));
    }

    @GetMapping("/{albumId}")
    public ResponseEntity<Album> getAlbum(@PathVariable String albumId){
        return ResponseEntity.ok(albumService.getAlbumById(albumId));
    }

    @GetMapping("/{albumId}/tracks")
    public ResponseEntity<List<Track>> getAlbumTracks(@PathVariable String albumId) {
        return ResponseEntity.ok(albumService.getTracksByAlbumId(albumId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewAlbum() {}

}
