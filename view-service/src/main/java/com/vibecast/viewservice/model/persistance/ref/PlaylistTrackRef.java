package com.vibecast.viewservice.model.persistance.ref;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistTrackRef implements Serializable {
    private LocalDateTime addedAt;
    private OwnerRef addedBy;
    private String trackId;
}

//{
//      "addedAt": "2024-07-09T12:00:00",
//      "addedBy": {
//        "userId": "user123",
//        "username": "Owner Username"
//      },
//      "trackId": "track1Id"
//    },

