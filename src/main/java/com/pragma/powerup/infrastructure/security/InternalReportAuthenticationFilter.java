package com.pragma.powerup.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class InternalReportAuthenticationFilter implements WebFilter {
    private static final String REPORT_PATH = "/internal/bootcamp-reports";
    private static final String TOKEN_HEADER = "X-Internal-Token";
    private final byte[] expectedToken;

    public InternalReportAuthenticationFilter(@Value("${report.internal-token}") String token) {
        this.expectedToken = token.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!exchange.getRequest().getPath().pathWithinApplication().value().equals(REPORT_PATH)) {
            return chain.filter(exchange);
        }
        String token = exchange.getRequest().getHeaders().getFirst(TOKEN_HEADER);
        if (token == null || !MessageDigest.isEqual(expectedToken, token.getBytes(StandardCharsets.UTF_8))) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }
}
