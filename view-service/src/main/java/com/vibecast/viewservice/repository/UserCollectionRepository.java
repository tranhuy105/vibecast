package com.vibecast.viewservice.repository;

import com.vibecast.viewservice.model.persistance.UserCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserCollectionRepository extends MongoRepository<UserCollection, String> {
}
