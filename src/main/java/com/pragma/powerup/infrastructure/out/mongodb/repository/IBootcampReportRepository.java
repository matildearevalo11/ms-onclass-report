package com.pragma.powerup.infrastructure.out.mongodb.repository;

import com.pragma.powerup.infrastructure.out.mongodb.entity.BootcampReportDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IBootcampReportRepository extends ReactiveMongoRepository<BootcampReportDocument, Long> {
}
