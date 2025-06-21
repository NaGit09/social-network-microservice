package com.example.api_gateway.routes;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AuthServiceRoutes {
    @Bean
    public RouteLocator AuthRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/auth/register")
                        .and().method("POST")
                        .uri("http://localhost:8081/auth/register"))
                .route(r -> r.path("/auth/login")
                        .and().method("POST")
                        .uri("http://localhost:8081/auth/login"))
                .build();
    }

}
