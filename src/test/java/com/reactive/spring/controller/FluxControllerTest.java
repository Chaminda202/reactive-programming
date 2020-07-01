package com.reactive.spring.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) this annotation can be added to load complete application context. No need to define RANDOM_PORT
@ExtendWith(SpringExtension.class) // no need to load complete application context
@WebFluxTest
@AutoConfigureWebTestClient(timeout = "10000") //10 seconds
public class FluxControllerTest {
    @Autowired
    WebTestClient webTestClient; // this is used to call non blocking api, By default, the WebTestClient will be timeout after 5 seconds

    @Test
    void testFluxFirstApproach() {
        Flux<Integer> integerFluxResponse = this.webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange() // actually invoke endpoint
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(integerFluxResponse)
                .expectSubscription()
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .verifyComplete();
    }

    @Test
    void testFluxSecondApproach() {
        this.webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .hasSize(5);
    }

    @Test
    void testFluxThirdApproach() {
        List<Integer> expectedResult = Arrays.asList(1, 2, 3, 4, 5);
        final EntityExchangeResult<List<Integer>> listEntityExchangeResult = this.webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();

        Assertions.assertEquals(expectedResult, listEntityExchangeResult.getResponseBody());
    }

    @Test
    void testFluxForthApproach() {
        List<Integer> expectedResult = Arrays.asList(1, 2, 3, 4, 5);
       this.webTestClient.get().uri("/flux")
               .accept(MediaType.APPLICATION_JSON)
               .exchange()
               .expectStatus().isOk()
               .expectBodyList(Integer.class)
               .consumeWith(response -> {
                   Assertions.assertEquals(expectedResult, response.getResponseBody());
               });
    }

    @Test
    void testFluxStreamApproach() {
        Flux<Integer> integerFluxResponse = this.webTestClient.get().uri("/fluxStream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange() // actually invoke endpoint
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(integerFluxResponse)
                .expectSubscription()
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .verifyComplete();
    }

    @Test
    void testInfiniteFluxStream() {
        Flux<Long> longFluxResponse = this.webTestClient.get().uri("/infiniteFluxStream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange() // actually invoke endpoint
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(longFluxResponse)
                .expectSubscription()
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .thenCancel()
                .verify();
    }
}
