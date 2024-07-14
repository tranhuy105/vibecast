package com.vibecast.viewservice.model.response;

import com.vibecast.viewservice.model.persistance.Track;
import com.vibecast.viewservice.model.persistance.ref.OwnerRef;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
public class PlaylistTrackResponseDto implements Serializable{
    private LocalDateTime addedAt;
    private OwnerRef addedBy;
    private Track track;
}
