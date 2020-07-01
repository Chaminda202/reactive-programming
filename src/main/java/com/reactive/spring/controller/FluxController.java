package com.reactive.spring.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class FluxController {
    @GetMapping(value = "/flux")
    public Flux<Integer> flux(){
        return Flux.just(1, 2, 3, 4, 5)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "/fluxStream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> fluxStream(){
        return Flux.just(1, 2, 3, 4, 5)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "/infiniteFluxStream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Long> infiniteFluxStream(){
        return Flux.interval(Duration.ofSeconds(1))
                .log();
    }
}