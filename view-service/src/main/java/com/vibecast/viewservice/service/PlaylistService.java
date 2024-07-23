package com.vibecast.viewservice.service;

import com.vibecast.viewservice.model.persistance.Playlist;
import com.vibecast.viewservice.model.persistance.ref.PlaylistTrackRef;
import com.vibecast.viewservice.model.response.PaginatedObjectDto;
import com.vibecast.viewservice.model.response.PlaylistSummary;
import com.vibecast.viewservice.model.response.PlaylistTrackResponseDto;
import com.vibecast.viewservice.model.response.PlaylistWithTracksResponseDto;
import com.vibecast.viewservice.repository.PlaylistRepository;
import com.vibecast.viewservice.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final TrackRepository trackRepository;
    private final CacheService cacheService;

    @Cacheable(value = "playlist", key = "#playlistId + '::page:' + #page")
    public PlaylistWithTracksResponseDto findPlaylistById(String playlistId, int page, int pageSize) {
        int limit = pageSize;
        int offset = (page-1)*pageSize;

        PlaylistSummary playlistDetails = playlistRepository.findPlaylistByIdWithTotalTrackCount(playlistId);
        if (playlistDetails == null) {
            throw new RuntimeException("Playlist not found");
        }
        List<PlaylistTrackResponseDto> paginatedTracks = playlistRepository.getPaginatedTracks(playlistId, limit, offset);
        int totalTracks = playlistDetails.getTracksCount();
        PaginatedObjectDto<PlaylistTrackResponseDto> paginatedObjectDto = new PaginatedObjectDto<>(limit, offset, totalTracks, paginatedTracks);

        return new PlaylistWithTracksResponseDto(
                playlistDetails.getId(),
                playlistDetails.getName(),
                playlistDetails.getDescription(),
                playlistDetails.getOwner(),
                playlistDetails.isPublic(),
                playlistDetails.isCollaborative(),
                playlistDetails.getPreviewImage(),
                paginatedObjectDto
        );
    }

    public List<PlaylistSummary> findByOwnerId(String ownerId) {
        return playlistRepository.findByOwnerUserIdWithTrackCount(ownerId);
    }

    public void addTracksToPlaylist(String playlistId, List<String> trackIds) {
        if (trackIds.isEmpty()) {
            throw new RuntimeException("Empty ids");
        }

        Set<String> uniqueTrackIds = new HashSet<>(trackIds);

        Integer existingTrackCount = trackRepository.countByIdIn(uniqueTrackIds);
        if (existingTrackCount != uniqueTrackIds.size()) {
            throw new RuntimeException("One or more track IDs do not exist");
        }

        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new RuntimeException("Playlist not found"));

        Set<String> existingTrackIds = playlist.getTracks().stream()
                .map(PlaylistTrackRef::getTrackId)
                .collect(Collectors.toSet());

        List<String> newTrackIds = uniqueTrackIds.stream()
                .filter(id -> !existingTrackIds.contains(id))
                .toList();

        if (newTrackIds.isEmpty()) {
            throw new RuntimeException("All tracks are already in the playlist");
        }

        for (String trackId : newTrackIds) {
            PlaylistTrackRef trackRef = new PlaylistTrackRef();
            trackRef.setAddedAt(LocalDateTime.now());
            trackRef.setAddedBy(playlist.getOwner());
            trackRef.setTrackId(trackId);
            playlist.getTracks().add(0,trackRef);
        }

        playlistRepository.save(playlist);
        evictAllPlaylistCache(playlistId);
    }

    public void removeTracksFromPlaylist(String playlistId, List<String> trackIds) {
        if (trackIds.isEmpty()) {
            throw new RuntimeException("Empty ids");
        }

        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new RuntimeException("Playlist not found"));

        List<PlaylistTrackRef> tracksToRemove = playlist.getTracks().stream()
                .filter(trackRef -> trackIds.contains(trackRef.getTrackId()))
                .toList();

        playlist.getTracks().removeAll(tracksToRemove);
        playlistRepository.save(playlist);
        evictAllPlaylistCache(playlistId);
    }

    private void evictAllPlaylistCache(String playlistId) {
        cacheService.evictAllPlaylistCache(playlistId);
    }
}
