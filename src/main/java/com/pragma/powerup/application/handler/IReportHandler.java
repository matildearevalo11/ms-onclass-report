package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.BootcampReportRequestDto;
import reactor.core.publisher.Mono;

public interface IReportHandler {
    Mono<Void> save(BootcampReportRequestDto request);
}
