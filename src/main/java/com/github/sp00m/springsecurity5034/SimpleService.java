package com.github.sp00m.springsecurity5034;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SimpleService {

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Mono<String> getProtectedHello() {
        return Mono.just("hello");
    }
    
}
