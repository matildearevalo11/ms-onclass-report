package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.request.BootcampReportRequestDto;
import com.pragma.powerup.application.dto.request.CapacityDetailRequestDto;
import com.pragma.powerup.application.dto.request.TechnologySummaryRequestDto;
import com.pragma.powerup.domain.model.BootcampReport;
import com.pragma.powerup.domain.model.CapacityDetail;
import com.pragma.powerup.domain.model.TechnologySummary;
import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class BootcampReportMapper {
    public BootcampReport toModel(BootcampReportRequestDto request) {
        return new BootcampReport(request.getBootcampId(), request.getName(), request.getDescription(),
                request.getLaunchDate(), request.getDurationDays(), mapAll(request.getCapacities(), this::toModel),
                request.getCapacityCount(), request.getTechnologyCount(), request.getPersonCount());
    }

    private CapacityDetail toModel(CapacityDetailRequestDto capacity) {
        return capacity == null ? null
                : new CapacityDetail(capacity.getId(), capacity.getName(), mapAll(capacity.getTechnologies(), this::toModel));
    }

    private TechnologySummary toModel(TechnologySummaryRequestDto technology) {
        return technology == null ? null : new TechnologySummary(technology.getId(), technology.getName());
    }

    private static <S, T> List<T> mapAll(List<S> source, Function<S, T> mapper) {
        return source == null ? null : source.stream().map(mapper).toList();
    }
}
