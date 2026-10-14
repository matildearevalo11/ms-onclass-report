package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IReportServicePort;
import com.pragma.powerup.domain.model.BootcampReport;
import com.pragma.powerup.domain.spi.IReportPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ReportUseCase implements IReportServicePort {
    private final IReportPersistencePort persistence;

    @Override
    public Mono<Void> save(BootcampReport report) {
        return persistence.save(report);
    }
}
