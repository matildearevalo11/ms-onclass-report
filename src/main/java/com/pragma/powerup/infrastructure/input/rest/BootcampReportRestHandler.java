package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.BootcampReportRequestDto;
import com.pragma.powerup.application.handler.IReportHandler;
import com.pragma.powerup.infrastructure.input.rest.support.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BootcampReportRestHandler {
    private final IReportHandler handler;
    private final RequestValidator validator;

    public Mono<ServerResponse> save(ServerRequest request) {
        return validator.body(request, BootcampReportRequestDto.class)
                .flatMap(handler::save)
                .then(ServerResponse.noContent().build());
    }
}
