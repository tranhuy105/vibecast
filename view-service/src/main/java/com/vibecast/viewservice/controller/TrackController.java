package com.vibecast.viewservice.controller;

import com.vibecast.viewservice.model.persistance.Track;
import com.vibecast.viewservice.service.S3Service;
import com.vibecast.viewservice.service.TrackService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

@RestController
@RequestMapping("/tracks")
@RequiredArgsConstructor
public class TrackController {
    private final TrackService trackService;
    private final S3Service s3Service;

    @GetMapping
    public void getSeveralTracks() {}

    @GetMapping("/{id}")
    public ResponseEntity<Track> getTrack(@PathVariable String id) {
        return ResponseEntity.ok(trackService.getTrackById(id));
    }

    @PostMapping
    public ResponseEntity<Void> uploadNewTrack(@RequestParam("trackFile") MultipartFile trackFile,
                                               @RequestParam("albumId") String albumId,
                                               @RequestParam("trackName") String trackName) {
        File file = convertMultipartFileToFile(trackFile);
        trackService.uploadNewTrack(file, albumId, trackName);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    private File convertMultipartFileToFile(MultipartFile multipartFile) {
        File file = File.createTempFile("temp", multipartFile.getOriginalFilename());

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }

        return file;
    }
}
