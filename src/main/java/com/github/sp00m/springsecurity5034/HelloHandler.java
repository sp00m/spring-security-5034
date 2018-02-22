package com.github.sp00m.springsecurity5034;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class HelloHandler {

    @Autowired
    private HelloService service;

    public Mono<ServerResponse> syncHello(ServerRequest request) {
        return service
                .getProtectedHello()
                .transform(hello -> ok().body(hello, String.class));
    }

    public Mono<ServerResponse> asyncHello(ServerRequest request) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> service.getProtectedHello().block());
        return Mono
                .fromFuture(future)
                .transform(hello -> ok().body(hello, String.class));
    }
    
}
