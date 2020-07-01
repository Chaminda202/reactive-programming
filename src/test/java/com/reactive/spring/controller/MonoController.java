package com.reactive.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class MonoController {

    @GetMapping(value = "/mono")
    public Mono<Integer> mono(){
        return Mono.just(1)
                .log();
    }
}
