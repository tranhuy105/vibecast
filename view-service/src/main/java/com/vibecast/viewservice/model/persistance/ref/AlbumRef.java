package com.vibecast.viewservice.model.persistance.ref;

import com.vibecast.viewservice.model.persistance.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumRef implements Serializable {
    private String id;
    private String name;
    private Image coverImage;
}
