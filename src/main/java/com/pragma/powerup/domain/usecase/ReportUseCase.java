package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IReportServicePort;
import com.pragma.powerup.domain.exception.ExceptionMessages;
import com.pragma.powerup.domain.exception.NotFoundException;
import com.pragma.powerup.domain.model.BootcampReport;
import com.pragma.powerup.domain.model.CapacityDetail;
import com.pragma.powerup.domain.model.TechnologySummary;
import com.pragma.powerup.domain.spi.IReportPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import static com.pragma.powerup.domain.util.DomainValidations.requireNotNegative;
import static com.pragma.powerup.domain.util.DomainValidations.requirePositive;
import static com.pragma.powerup.domain.util.DomainValidations.requirePresent;
import static com.pragma.powerup.domain.util.DomainValidations.requireText;

@RequiredArgsConstructor
public class ReportUseCase implements IReportServicePort {
    private final IReportPersistencePort persistence;

    @Override
    public Mono<Void> save(BootcampReport report) {
        return Mono.fromCallable(() -> validate(report)).flatMap(persistence::save);
    }

    @Override
    public Mono<Void> updatePersonCount(Long bootcampId, Integer personCount) {
        return Mono.fromCallable(() -> {
                    requirePositive(bootcampId, ExceptionMessages.BOOTCAMP_ID_INVALID);
                    return requireNotNegative(personCount, ExceptionMessages.COUNTS_INVALID);
                })
                .flatMap(count -> persistence.updatePersonCount(bootcampId, count))
                .flatMap(updated -> Boolean.TRUE.equals(updated) ? Mono.<Void>empty()
                        : Mono.error(new NotFoundException(ExceptionMessages.REPORT_NOT_FOUND.getMessage())));
    }

    private BootcampReport validate(BootcampReport report) {
        requirePositive(report.getBootcampId(), ExceptionMessages.BOOTCAMP_ID_INVALID);
        requireText(report.getName(), ExceptionMessages.BOOTCAMP_NAME_REQUIRED);
        requireText(report.getDescription(), ExceptionMessages.BOOTCAMP_DESCRIPTION_REQUIRED);
        requirePresent(report.getLaunchDate(), ExceptionMessages.LAUNCH_DATE_REQUIRED);
        requirePositive(report.getDurationDays(), ExceptionMessages.DURATION_INVALID);
        requirePresent(report.getCapacities(), ExceptionMessages.CAPACITIES_REQUIRED).forEach(this::validateCapacity);
        requireNotNegative(report.getCapacityCount(), ExceptionMessages.COUNTS_INVALID);
        requireNotNegative(report.getTechnologyCount(), ExceptionMessages.COUNTS_INVALID);
        requireNotNegative(report.getPersonCount(), ExceptionMessages.COUNTS_INVALID);
        return report;
    }

    private void validateCapacity(CapacityDetail capacity) {
        requirePresent(capacity, ExceptionMessages.CAPACITY_INVALID);
        requirePositive(capacity.getId(), ExceptionMessages.CAPACITY_INVALID);
        requireText(capacity.getName(), ExceptionMessages.CAPACITY_INVALID);
        requirePresent(capacity.getTechnologies(), ExceptionMessages.CAPACITY_INVALID).forEach(this::validateTechnology);
    }

    private void validateTechnology(TechnologySummary technology) {
        requirePresent(technology, ExceptionMessages.TECHNOLOGY_INVALID);
        requirePositive(technology.getId(), ExceptionMessages.TECHNOLOGY_INVALID);
        requireText(technology.getName(), ExceptionMessages.TECHNOLOGY_INVALID);
    }
}
