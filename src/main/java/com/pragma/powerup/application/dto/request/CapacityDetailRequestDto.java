package com.pragma.powerup.application.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CapacityDetailRequestDto {
    private Long id;
    private String name;
    private List<TechnologySummaryRequestDto> technologies;
}
