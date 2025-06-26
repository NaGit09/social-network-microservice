package com.example.api_gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ErrorHandlingFilter implements GatewayFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).doOnTerminate(() -> {
            HttpStatusCode code = exchange.getResponse().getStatusCode();
            if (code == HttpStatus.INTERNAL_SERVER_ERROR || code == HttpStatus.NOT_FOUND) {
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            }
        });
    }
}
