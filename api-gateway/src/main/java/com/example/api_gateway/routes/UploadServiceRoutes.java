package com.example.api_gateway.routes;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UploadServiceRoutes {
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes().route(
                r -> r.path("/upload/uploadFile/")
                        .and().method("POST")
                        .uri("http://localhost:8082/upload/uploadFile/")
        )
                .route(r -> r.path("/upload/delete/")
                        .and().method("DELETE")
                        .uri("http://localhost:8082/delete/"))

                .build();
    }
}
