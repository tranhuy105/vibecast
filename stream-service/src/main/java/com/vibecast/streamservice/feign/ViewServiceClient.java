package com.vibecast.streamservice.feign;

import com.vibecast.streamservice.feign.model.Track;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "view-service", url = "http://localhost:8080")
public interface ViewServiceClient {
    @GetMapping("/tracks/{trackId}")
    Track getTrackById(@PathVariable("trackId") String trackId);
}
