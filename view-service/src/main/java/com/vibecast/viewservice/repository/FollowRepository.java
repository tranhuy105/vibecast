package com.vibecast.viewservice.repository;

import com.vibecast.viewservice.model.persistance.Follow;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FollowRepository extends MongoRepository<Follow, String> {
}
