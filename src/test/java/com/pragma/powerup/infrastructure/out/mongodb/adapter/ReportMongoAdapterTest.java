package com.pragma.powerup.infrastructure.out.mongodb.adapter;

import com.pragma.powerup.domain.model.BootcampReport;
import com.pragma.powerup.domain.model.CapacityDetail;
import com.pragma.powerup.domain.model.TechnologySummary;
import com.pragma.powerup.infrastructure.out.mongodb.entity.BootcampReportDocument;
import com.pragma.powerup.infrastructure.out.mongodb.repository.IBootcampReportRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReportMongoAdapterTest {
    @Test
    void storesSnapshotUnderBootcampId() {
        IBootcampReportRepository repository = mock(IBootcampReportRepository.class);
        when(repository.save(any(BootcampReportDocument.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        var report = new BootcampReport(7L, "Backend", "Services", LocalDate.of(2026, 11, 1), 90,
                List.of(new CapacityDetail(1L, "API", List.of(new TechnologySummary(3L, "Java")))), 1, 1L, 0);

        StepVerifier.create(new ReportMongoAdapter(repository).save(report)).verifyComplete();

        var saved = ArgumentCaptor.forClass(BootcampReportDocument.class);
        verify(repository).save(saved.capture());
        assertThat(saved.getValue().getBootcampId()).isEqualTo(7L);
        assertThat(saved.getValue().getCapacities().getFirst().getTechnologies().getFirst().getId()).isEqualTo(3L);
        assertThat(saved.getValue().getTechnologyCount()).isEqualTo(1);
    }
}
