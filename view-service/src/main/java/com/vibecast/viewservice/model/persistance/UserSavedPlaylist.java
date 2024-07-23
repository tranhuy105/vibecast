package com.vibecast.viewservice.model.persistance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "user_saved_playlists")
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "userId_savedAt_idx", def = "{'userId': 1, 'savedAt': -1}")
})
public class UserSavedPlaylist {
    @Id
    private String id;
    private String userId;
    private String playlistId;
    private LocalDateTime savedAt;
}
