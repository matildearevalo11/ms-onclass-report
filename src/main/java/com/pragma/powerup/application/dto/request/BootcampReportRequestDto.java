package com.pragma.powerup.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BootcampReportRequestDto {
    @NotNull
    @Positive
    private Long bootcampId;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private LocalDate launchDate;
    @NotNull
    @Positive
    private Integer durationDays;
    @NotNull
    private List<@Valid CapacityDetailRequestDto> capacities;
    @NotNull
    @Min(0)
    private Integer capacityCount;
    @NotNull
    @Min(0)
    private Long technologyCount;
    @NotNull
    @Min(0)
    private Integer personCount;
}
