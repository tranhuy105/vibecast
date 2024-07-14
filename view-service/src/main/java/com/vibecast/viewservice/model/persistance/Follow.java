package com.vibecast.viewservice.model.persistance;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Getter
@Setter
@Document
@CompoundIndex(name = "user_artist_index", def = "{'userId': 1, 'artistId': 1}", unique = true)
@AllArgsConstructor
@NoArgsConstructor

public class Follow {
    @Id
    private String id;
    @NotNull
    private String userId;
    @NotNull
    private String artistId;
    private LocalDateTime followedAt;
}