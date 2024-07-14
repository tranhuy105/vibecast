package com.vibecast.viewservice.model.response;

import com.vibecast.viewservice.model.persistance.Image;
import com.vibecast.viewservice.model.persistance.ref.OwnerRef;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistSummary {
    private String id;
    private String name;
    private String description;
    private OwnerRef owner;
    private boolean isPublic;
    private boolean isCollaborative;
    private Image previewImage;
    private Integer tracksCount;
}