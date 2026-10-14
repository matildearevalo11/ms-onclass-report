package com.pragma.powerup.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CapacityDetailRequestDto {
    @NotNull
    @Positive
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private List<@Valid TechnologySummaryRequestDto> technologies;
}
