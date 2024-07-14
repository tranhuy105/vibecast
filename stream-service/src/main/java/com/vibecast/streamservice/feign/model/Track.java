package com.vibecast.streamservice.feign.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Track {
    private String id;
    private String name;
    private Long duration;
    private AlbumRef album;
    private List<ArtistRef> artists;
}