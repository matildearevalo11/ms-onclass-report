package com.pragma.powerup.infrastructure.out.mongodb.adapter;

import com.pragma.powerup.domain.model.BootcampReport;
import com.pragma.powerup.domain.spi.IReportPersistencePort;
import com.pragma.powerup.infrastructure.out.mongodb.entity.BootcampReportDocument;
import com.pragma.powerup.infrastructure.out.mongodb.repository.IBootcampReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ReportMongoAdapter implements IReportPersistencePort {
    private static final String ID_FIELD = "_id";
    private static final String PERSON_COUNT_FIELD = "personCount";

    private final IBootcampReportRepository repository;
    private final ReactiveMongoTemplate template;

    @Override
    public Mono<Void> save(BootcampReport report) {
        return repository.save(BootcampReportDocument.from(report)).then();
    }

    @Override
    public Mono<Boolean> updatePersonCount(Long bootcampId, Integer personCount) {
        return template.updateFirst(Query.query(Criteria.where(ID_FIELD).is(bootcampId)),
                        Update.update(PERSON_COUNT_FIELD, personCount), BootcampReportDocument.class)
                .map(result -> result.getMatchedCount() > 0);
    }
}
