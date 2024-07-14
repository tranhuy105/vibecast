package com.vibecast.viewservice.service;

import com.vibecast.viewservice.model.persistance.Album;
import com.vibecast.viewservice.model.persistance.Track;
import com.vibecast.viewservice.model.persistance.ref.AlbumRef;
import com.vibecast.viewservice.repository.AlbumRepository;
import com.vibecast.viewservice.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class TrackService {
    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final S3Service s3Service;

    @Cacheable(value = "tracks", key = "#id")
    public Track getTrackById(String id) {
        return trackRepository.findById(id).orElseThrow();
    }

    public void uploadNewTrack(File trackFile, String albumId, String trackName){
        Album album = albumRepository.findById(albumId).
                orElseThrow(() -> new RuntimeException("Album not found for id: "+albumId));

        Track newTrack = new Track();
        newTrack.setName(trackName);
        newTrack.setAlbum(new AlbumRef(album.getId(), album.getName(), album.getCoverImage()));
        newTrack.setArtists(album.getArtists());
        // need to extract track duration
        newTrack.setDuration(0L);
        Track savedTrack = trackRepository.save(newTrack);
        s3Service.uploadTrack(trackFile, savedTrack.getId());
    }
}
