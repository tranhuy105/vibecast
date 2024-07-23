package com.vibecast.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping("/fallback")
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public Mono<String> fallback() {
        return Mono.just("An error occurred on upstream services. Please try again later");
    }
}
