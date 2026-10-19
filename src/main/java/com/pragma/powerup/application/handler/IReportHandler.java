package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.BootcampReportRequestDto;
import com.pragma.powerup.application.dto.request.PersonCountRequestDto;
import reactor.core.publisher.Mono;

public interface IReportHandler {
    Mono<Void> save(BootcampReportRequestDto request);
    Mono<Void> updatePersonCount(Long bootcampId, PersonCountRequestDto request);
}
