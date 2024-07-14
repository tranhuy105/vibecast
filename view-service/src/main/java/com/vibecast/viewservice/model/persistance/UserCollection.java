package com.vibecast.viewservice.model.persistance;

import com.vibecast.viewservice.model.persistance.ref.CreatedPlaylistRef;
import com.vibecast.viewservice.model.persistance.ref.SavedAlbumRef;
import com.vibecast.viewservice.model.persistance.ref.SavedPlaylistRef;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
public class UserCollection {
    @Id
    private String id;
    private List<SavedAlbumRef> savedAlbumRefs;
    private List<SavedPlaylistRef> savedPlaylists;
    private List<CreatedPlaylistRef> createdPlaylistRefs;
}

//{
//  "_id": "userId",
//  "saved_albums": [
//    {
//      "album_id": "albumId",
//      "saved_at": "2024-07-08T12:38:07.417Z"
//    }
//  ],
//  "saved_playlists": [
//    {
//      "playlist_id": "playlistId",
//      "saved_at": "2024-07-08T12:38:07.417Z"
//    }
//  ],
//  "created_playlists": [
//    {
//      "playlist_id": "playlistId",
//      "created_at": "2024-07-08T12:38:07.417Z"
//    }
//  ]
//}
