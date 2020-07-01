package com.reactive.spring.mono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoTest {

    @Test
    public void monoTest() {
        Mono<String> mono = Mono.just("Spring")
                .log();

        mono.subscribe(System.out::println);
    }

    @Test
    public void monoTestVerify() {
        Mono<String> mono = Mono.just("Spring")
                .log();

        StepVerifier.create(mono)
                .expectNext("Spring")
                .verifyComplete();
    }

    @Test
    public void monoTestVerifyCount() {
        Mono<String> mono = Mono.just("Spring")
                .log();

        StepVerifier.create(mono)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void monoTestWithError() {
        Mono<Object> monoError = Mono.error(new RuntimeException("Error occurred")).log();
        monoError.subscribe(System.out::println,
                e -> System.err.println("Exception is " + e));
    }

    @Test
    public void monoTestWithErrorVerify() {
        StepVerifier.create(Mono.error(new RuntimeException("Error occurred")).log())
                // .expectError(RuntimeException.class)
                .expectErrorMessage("Error occurred")
                .verify();
    }
}
