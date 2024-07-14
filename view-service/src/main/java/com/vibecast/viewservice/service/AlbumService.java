package com.vibecast.viewservice.service;

import com.vibecast.viewservice.model.persistance.Album;
import com.vibecast.viewservice.model.persistance.Track;
import com.vibecast.viewservice.model.response.PaginatedObjectDto;
import com.vibecast.viewservice.repository.AlbumRepository;
import com.vibecast.viewservice.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;

    public PaginatedObjectDto<Album> findAll(int page, int pageSize) {
        page = page - 1;
        var albumPage =  albumRepository.findAll(PageRequest.of(page, pageSize));
        return new PaginatedObjectDto<>(pageSize, page * pageSize, (int) albumPage.getTotalElements(), albumPage.getContent());
    }

    @Cacheable(value = "albums", key = "#id")
    public Album getAlbumById(String id) {
        return albumRepository.findById(id).orElseThrow();
    }

//    @Cacheable(value = "albumTracks", key = "#id")
    public List<Track> getTracksByAlbumId(String id) {
        return trackRepository.findByAlbumId(id);
    }
}
