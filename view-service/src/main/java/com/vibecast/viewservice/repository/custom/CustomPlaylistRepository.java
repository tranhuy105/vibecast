package com.vibecast.viewservice.repository.custom;

import com.vibecast.viewservice.model.persistance.Playlist;
import com.vibecast.viewservice.model.response.PlaylistSummary;
import com.vibecast.viewservice.model.response.PlaylistTrackResponseDto;
import com.vibecast.viewservice.model.response.PlaylistWithTracksResponseDto;

import java.util.List;

public interface CustomPlaylistRepository {
//    PlaylistWithTracksResponseDto getPlaylistWithTrackDetails(String playlistId);

    List<PlaylistSummary> findByOwnerUserIdWithTrackCount(String playlistId);

    PlaylistSummary findPlaylistByIdWithTotalTrackCount(String playlistId);

    List<PlaylistTrackResponseDto> getPaginatedTracks(String playlistId, int limit, int offset);
}
