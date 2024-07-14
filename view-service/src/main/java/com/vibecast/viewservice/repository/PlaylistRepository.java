package com.vibecast.viewservice.repository;

import com.vibecast.viewservice.model.persistance.Playlist;
import com.vibecast.viewservice.repository.custom.CustomPlaylistRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends MongoRepository<Playlist, String>, CustomPlaylistRepository {
    List<Playlist> findByOwnerUserId(String userId);

    @Query(value = "{'id': ?0}",
            fields = "{'name': 1, 'description': 1, 'owner': 1, 'isPublic': 1, 'isCollaborative': 1, 'previewImage': 1}")
    Optional<Playlist> findPlaylistWithoutTracks(String playlistId);
}
