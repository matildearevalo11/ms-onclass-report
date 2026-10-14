package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.request.BootcampReportRequestDto;
import com.pragma.powerup.domain.model.BootcampReport;
import com.pragma.powerup.domain.model.CapacityDetail;
import com.pragma.powerup.domain.model.TechnologySummary;
import org.springframework.stereotype.Component;

@Component
public class BootcampReportMapper {
    public BootcampReport toModel(BootcampReportRequestDto request) {
        return new BootcampReport(request.getBootcampId(), request.getName(), request.getDescription(),
                request.getLaunchDate(), request.getDurationDays(),
                request.getCapacities().stream().map(capacity -> new CapacityDetail(
                        capacity.getId(), capacity.getName(), capacity.getTechnologies().stream()
                                .map(technology -> new TechnologySummary(technology.getId(), technology.getName()))
                                .toList())).toList(),
                request.getCapacityCount(), request.getTechnologyCount(), request.getPersonCount());
    }
}
