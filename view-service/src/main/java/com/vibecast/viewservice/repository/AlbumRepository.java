package com.vibecast.viewservice.repository;

import com.vibecast.viewservice.model.persistance.Album;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends MongoRepository<Album, String> {
    @Query(value = "{ 'artists.id' : ?0 }")
    List<Album> findByArtistsId(String artistId);
}
