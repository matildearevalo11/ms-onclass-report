package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.ExceptionMessages;
import com.pragma.powerup.domain.exception.ValidationException;
import com.pragma.powerup.domain.model.BootcampReport;
import com.pragma.powerup.domain.model.CapacityDetail;
import com.pragma.powerup.domain.model.TechnologySummary;
import com.pragma.powerup.domain.spi.IReportPersistencePort;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReportUseCaseTest {
    private static final LocalDate LAUNCH = LocalDate.of(2026, 11, 1);
    private static final List<CapacityDetail> CAPACITIES =
            List.of(new CapacityDetail(1L, "Backend", List.of(new TechnologySummary(3L, "Java"))));

    private final IReportPersistencePort persistence = mock(IReportPersistencePort.class);
    private final ReportUseCase useCase = new ReportUseCase(persistence);

    private static BootcampReport report(Long id, String name, Integer duration, List<CapacityDetail> capacities,
            Integer personCount) {
        return new BootcampReport(id, name, "Services", LAUNCH, duration, capacities, 1, 1L, personCount);
    }

    @Test
    void persistsAValidReport() {
        var report = report(7L, "Backend", 90, CAPACITIES, 0);
        when(persistence.save(report)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.save(report)).verifyComplete();
        verify(persistence).save(report);
    }

    @Test
    void rejectsInvalidReportsWithoutPersistingThem() {
        var invalid = Map.of(
                report(0L, "Backend", 90, CAPACITIES, 0), ExceptionMessages.BOOTCAMP_ID_INVALID,
                report(7L, " ", 90, CAPACITIES, 0), ExceptionMessages.BOOTCAMP_NAME_REQUIRED,
                report(7L, "Backend", 0, CAPACITIES, 0), ExceptionMessages.DURATION_INVALID,
                report(7L, "Backend", 90, null, 0), ExceptionMessages.CAPACITIES_REQUIRED,
                report(7L, "Backend", 90, List.of(new CapacityDetail(1L, "", List.of())), 0), ExceptionMessages.CAPACITY_INVALID,
                report(7L, "Backend", 90, List.of(new CapacityDetail(1L, "API",
                        List.of(new TechnologySummary(null, "Java")))), 0), ExceptionMessages.TECHNOLOGY_INVALID,
                report(7L, "Backend", 90, CAPACITIES, -1), ExceptionMessages.COUNTS_INVALID);

        invalid.forEach((report, message) -> StepVerifier.create(useCase.save(report))
                .expectErrorSatisfies(error -> assertThat(error)
                        .isInstanceOf(ValidationException.class).hasMessage(message.getMessage()))
                .verify());
        verifyNoInteractions(persistence);
    }

    @Test
    void propagatesPersistenceFailures() {
        var report = report(7L, "Backend", 90, CAPACITIES, 0);
        when(persistence.save(report)).thenReturn(Mono.error(new IllegalStateException("Mongo down")));

        StepVerifier.create(useCase.save(report)).expectError(IllegalStateException.class).verify();
    }
}
