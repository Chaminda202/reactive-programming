package com.reactive.spring.flux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxTest {

    @Test
    public void testFlux(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .log();
        stringFlux
                .subscribe(System.out::println);
    }

    @Test
    public void testFluxAddingError(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();
        stringFlux
                .subscribe(System.out::println,
                        e -> System.err.println("Exception is " + e));
    }

    @Test
    public void testFluxAddingWithOutError(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                // .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();
        stringFlux
                .subscribe(System.out::println,
                        e -> System.err.println("Exception is " + e));
    }

    @Test
    public void testFluxEmittItemAfterError(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .concatWith(Flux.just("After emitted error"))
                .log();

        stringFlux
                .subscribe(System.out::println,
                        e -> System.err.println("Exception is " + e),
                        () -> System.out.println("Completed"));
    }

    // verify response in flux
    @Test
    public void testFluxElementsWithOutError(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .verifyComplete(); // verify complete is required to execute the statement, it is like stream
    }

    @Test
    public void testFluxElementsWithOutErrorSingleLineVerify(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Spring Boot", "Reactive Spring")
                .verifyComplete(); // verify complete is required to execute the statement, it is like stream
    }

    @Test
    public void testFluxElementsWithError(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                // .expectError(RuntimeException.class)
                .expectErrorMessage("Exception occurred")
                .verify(); // verify is required to execute the statement, it is like stream
    }

    @Test
    public void testFluxElementsCountWithError(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                // .expectError(RuntimeException.class)
                .expectErrorMessage("Exception occurred")
                .verify(); // verify is required to execute the statement, it is like stream
    }
}
