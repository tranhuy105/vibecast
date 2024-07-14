package com.vibecast.viewservice.model.persistance;

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
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Album implements Serializable {
    @Id
    private String id;
    @NotNull
    @Size(min = 1, max = 30)
    private String name;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    private Image coverImage;
    @NotEmpty
    private List<ArtistRef> artists;
}

//{
//  "_id": "albumId",
//  "name": "Album Name",
//  "release_date": "1981-12",
//  "cover_image": {
//      "url": "https://example.com/album/image",
//      "height": 300,
//      "width": 300
//    },
//  "artists": [
//    {
//      "id": "artistId",
//      "name": "Artist Name"
//    }
//  ]
//}