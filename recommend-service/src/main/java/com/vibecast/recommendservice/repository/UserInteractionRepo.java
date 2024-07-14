package com.vibecast.recommendservice.repository;

import com.vibecast.recommendservice.model.UserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInteractionRepo extends JpaRepository<UserInteraction, Long> {
}
