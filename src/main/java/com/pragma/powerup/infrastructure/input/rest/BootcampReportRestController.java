package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.BootcampReportRequestDto;
import com.pragma.powerup.application.handler.IReportHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/internal/bootcamp-reports")
@RequiredArgsConstructor
public class BootcampReportRestController {
    private final IReportHandler handler;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> save(@Valid @RequestBody BootcampReportRequestDto request) {
        return handler.save(request);
    }
}
