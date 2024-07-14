package com.vibecast.recommendservice.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserInteraction {
    @EmbeddedId
    private UserInteractionId id;
    private Double preference;
    @UpdateTimestamp
    private LocalDateTime interactionTime;
}
