package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.BootcampReport;
import reactor.core.publisher.Mono;

public interface IReportServicePort {
    Mono<Void> save(BootcampReport report);
}
