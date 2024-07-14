package com.vibecast.viewservice.model.response;

import com.vibecast.viewservice.model.persistance.Image;
import com.vibecast.viewservice.model.persistance.ref.OwnerRef;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class PlaylistWithTracksResponseDto implements Serializable {
        @Id
        private String id;
        private String name;
        private String description;
        private OwnerRef owner;
        private Boolean isPublic;
        private Boolean isCollaborative;
        private Image previewImage;
        private PaginatedObjectDto<PlaylistTrackResponseDto> tracks;
//        private List<PlaylistTrackResponseDto> tracks;
}
