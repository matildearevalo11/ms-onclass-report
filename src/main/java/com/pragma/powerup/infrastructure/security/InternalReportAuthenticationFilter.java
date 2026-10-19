package com.pragma.powerup.infrastructure.security;

import com.pragma.powerup.domain.exception.ExceptionMessages;
import com.pragma.powerup.infrastructure.constants.ApiPaths;
import com.pragma.powerup.infrastructure.constants.SecurityConstants;
import com.pragma.powerup.infrastructure.exceptionhandler.ErrorResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import tools.jackson.databind.json.JsonMapper;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class InternalReportAuthenticationFilter implements WebFilter {
    private final byte[] expectedToken;
    private final JsonMapper mapper;

    public InternalReportAuthenticationFilter(@Value("${report.internal-token}") String token, JsonMapper mapper) {
        this.expectedToken = token.getBytes(StandardCharsets.UTF_8);
        this.mapper = mapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!exchange.getRequest().getPath().pathWithinApplication().value().equals(ApiPaths.BOOTCAMP_REPORTS)) {
            return chain.filter(exchange);
        }
        String token = exchange.getRequest().getHeaders().getFirst(SecurityConstants.INTERNAL_TOKEN_HEADER);
        if (token == null || !MessageDigest.isEqual(expectedToken, token.getBytes(StandardCharsets.UTF_8))) {
            var response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            byte[] body = mapper.writeValueAsBytes(
                    ErrorResponse.from(ExceptionMessages.AUTHENTICATION_REQUIRED.getMessage()));
            return response.writeWith(Mono.just(response.bufferFactory().wrap(body)));
        }
        return chain.filter(exchange);
    }
}
