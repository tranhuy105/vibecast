package com.vibecast.viewservice.model.persistance;

import com.vibecast.viewservice.model.persistance.ref.OwnerRef;
import com.vibecast.viewservice.model.persistance.ref.PlaylistTrackRef;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "owner_userId_idx", def = "{'owner.userId': 1}")
})
public class Playlist {
    @Id
    private String id;
    @NotNull
    @NotEmpty
    private String name;
    private String description;
    @NotNull
    private OwnerRef owner;
    private boolean isPublic;
    private boolean isCollaborative;
    private long savedCount;
    @NotNull
    private List<PlaylistTrackRef> tracks;
    private Image previewImage;
}

//{
//  "id": "playlistId",
//  "name": "My Playlist",
//  "description": "Playlist Description",
//  "owner": {
//    "userId": "user123",
//    "username": "Owner Username"
//  },
//  "isPublic": true,
//  "isCollaborative": false,
//  "tracks": [
//    {
//      "addedAt": "2024-07-09T12:00:00",
//      "addedBy": {
//        "userId": "user123",
//        "username": "Owner Username"
//      },
//      "trackId": "track1Id"
//    },
//    {
//      "addedAt": "2024-07-09T12:15:00",
//      "addedBy": {
//        "userId": "user123",
//        "username": "Owner Username"
//      },
//      "trackId": "track2Id"
//    },
//    {
//      "addedAt": "2024-07-09T12:30:00",
//      "addedBy": {
//        "userId": "user123",
//        "username": "Owner Username"
//      },
//      "trackId": "track3Id"
//    }
//  ],
//  "previewImage": {
//    "url": "https://example.com/playlist-preview.jpg",
//    "height": 300,
//    "width": 300
//  }
//}
