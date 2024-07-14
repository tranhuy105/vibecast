package com.vibecast.viewservice.service;

import com.vibecast.viewservice.model.persistance.Album;
import com.vibecast.viewservice.model.persistance.Artist;
import com.vibecast.viewservice.repository.AlbumRepository;
import com.vibecast.viewservice.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }


    @Cacheable(value = "artists", key = "#id")
    public Artist getArtistById(String id) throws Exception {
        return artistRepository.findById(id).orElseThrow(() -> new Exception("Artist not found"));
    }

    @Cacheable(value = "artistAlbums", key = "#id")
    public List<Album> getAlbumsByArtistId(String id) {
        return albumRepository.findByArtistsId(id);
    }

    @Cacheable(value = "artistTopTracks", key = "#id")
    public List<Object> getTopTracksByArtistId(String id) {
        return new ArrayList<>();
    }

}
