package com.vibecast.viewservice.repository;

import com.vibecast.viewservice.model.persistance.Track;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Set;

public interface TrackRepository extends MongoRepository<Track, String> {
    @Query("{ 'album._id': ObjectId(?0) }")
    List<Track> findByAlbumId(String albumId);

    Integer countByIdIn(Set<String> ids);
}
