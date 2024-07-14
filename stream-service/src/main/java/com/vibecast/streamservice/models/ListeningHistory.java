package com.vibecast.streamservice.models;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class ListeningHistory implements Serializable {
    private String userId;
    private String trackId;
    private long listeningTime; // in milliseconds
}
