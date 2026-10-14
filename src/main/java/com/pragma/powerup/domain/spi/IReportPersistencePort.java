package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.BootcampReport;
import reactor.core.publisher.Mono;

public interface IReportPersistencePort {
    Mono<Void> save(BootcampReport report);
}
