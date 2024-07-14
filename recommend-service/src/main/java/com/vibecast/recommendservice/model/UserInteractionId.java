package com.vibecast.recommendservice.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class UserInteractionId implements Serializable {
    private Long userId;
    private Long trackId;
}