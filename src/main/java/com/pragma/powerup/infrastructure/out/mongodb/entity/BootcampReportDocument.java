package com.pragma.powerup.infrastructure.out.mongodb.entity;

import com.pragma.powerup.domain.model.BootcampReport;
import com.pragma.powerup.domain.model.CapacityDetail;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@AllArgsConstructor
@Document("bootcamp_reports")
public class BootcampReportDocument {
    @Id
    private final Long bootcampId;
    private final String name;
    private final String description;
    private final LocalDate launchDate;
    private final Integer durationDays;
    private final List<CapacityDetail> capacities;
    private final int capacityCount;
    private final long technologyCount;
    private final int personCount;

    public static BootcampReportDocument from(BootcampReport report) {
        return new BootcampReportDocument(report.getBootcampId(), report.getName(), report.getDescription(),
                report.getLaunchDate(), report.getDurationDays(), report.getCapacities(),
                report.getCapacityCount(), report.getTechnologyCount(), report.getPersonCount());
    }
}
