package com.vibecast.viewservice.repository;

import com.vibecast.viewservice.model.persistance.Artist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends MongoRepository<Artist, String> {
    boolean existsByName(String name);
}
