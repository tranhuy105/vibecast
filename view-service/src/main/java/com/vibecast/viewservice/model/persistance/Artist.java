package com.vibecast.viewservice.model.persistance;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Artist implements Serializable {
    @Id
    private String id;
    @Indexed(unique = true) // immutable
    @NotNull
    @Size(min = 1, max = 30)
    private String name;
    @NotNull
    private List<String> genres;
    @NotNull
    @Size(min = 10, max = 1000)
    private String bio;
    private Image profileImage;
    private Image bioImage;
}

//{
//  "_id": "artistId",
//  "name": "Artist Name",
//  "genres": ["genre1", "genre2"],
//  "bio": "Artist bio",
//  "images": [
//    {
//      "url": "https://example.com/artist/image",
//      "height": 300,
//      "width": 300
//    }
//  ]
//}