package com.pragma.powerup.infrastructure.out.mongodb.adapter;

import com.pragma.powerup.domain.model.BootcampReport;
import com.pragma.powerup.domain.spi.IReportPersistencePort;
import com.pragma.powerup.infrastructure.out.mongodb.entity.BootcampReportDocument;
import com.pragma.powerup.infrastructure.out.mongodb.repository.IBootcampReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ReportMongoAdapter implements IReportPersistencePort {
    private final IBootcampReportRepository repository;

    @Override
    public Mono<Void> save(BootcampReport report) {
        return repository.save(BootcampReportDocument.from(report)).then();
    }
}
