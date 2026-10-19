package com.pragma.powerup.application.dto.request;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BootcampReportRequestDto {
    private Long bootcampId;
    private String name;
    private String description;
    private LocalDate launchDate;
    private Integer durationDays;
    private List<CapacityDetailRequestDto> capacities;
    private Integer capacityCount;
    private Long technologyCount;
    private Integer personCount;
}
