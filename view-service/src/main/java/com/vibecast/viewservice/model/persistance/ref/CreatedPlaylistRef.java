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
public class CreatedPlaylistRef implements Serializable {
    private String playlistId;
    private LocalDateTime createdAt;
}
