package com.vibecast.viewservice.model.persistance;

import com.vibecast.viewservice.model.persistance.ref.AlbumRef;
import com.vibecast.viewservice.model.persistance.ref.ArtistRef;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Track implements Serializable{
    @Id
    private String id;
    @NotNull
    @NotEmpty
    @Size(min = 1,max = 30)
    private String name;
    private Long duration;
    @NotNull
    private AlbumRef album;
    @NotNull
    private List<ArtistRef> artists;
}

//{
//    "_id": ObjectId("track1Id"),
//    "name": "Track Name",
//    "album": {
//        "albumId": ObjectId("albumId"),
//        "name": "Album Name",
//        "coverImage": {
//            "url": "https://example.com/album-cover.jpg",
//            "height": 600,
//            "width": 600
//        }
//    },
//    "artists": [
//        {
//            "artistId": ObjectId("artist1Id"),
//            "name": "Artist Name"
//        }
//    ]
//}
