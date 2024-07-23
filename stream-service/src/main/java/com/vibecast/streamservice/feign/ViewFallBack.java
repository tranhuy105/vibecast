package com.vibecast.streamservice.feign;

import com.vibecast.streamservice.feign.model.Track;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ViewFallBack implements ViewServiceClient{
    @Override
    public Track getTrackById(String trackId) {
        return new Track(trackId, null, null, null, new ArrayList<>());
    }
}
