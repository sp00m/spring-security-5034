package com.github.sp00m.springsecurity5034;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.security.config.web.server.ServerHttpSecurity.http;
import static org.springframework.security.core.userdetails.User.withUsername;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SpringSecurity5034Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurity5034Application.class, args);
	}
	
	@Bean
	public ReactiveUserDetailsService userDetailsService() {
		UserDetails username = withUsername("username").password("password").roles("USER").build();
		return new MapReactiveUserDetailsService(username);
	}

	@Bean
	public ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService) {
		UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
		authenticationManager.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		return authenticationManager;
	}

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ReactiveAuthenticationManager authenticationManager) {
		return http()
				.authenticationManager(authenticationManager)
				.csrf().disable()
				.httpBasic().and()
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> endpoint(SimpleService service) {
		return route(
				GET("/hello"),
				request -> service
                        .getProtectedHello()
                        .transform(hello -> ok().body(hello, String.class))
		);
	}
	
}
