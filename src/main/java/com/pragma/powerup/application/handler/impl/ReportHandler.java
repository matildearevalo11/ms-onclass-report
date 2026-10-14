package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.BootcampReportRequestDto;
import com.pragma.powerup.application.handler.IReportHandler;
import com.pragma.powerup.application.mapper.BootcampReportMapper;
import com.pragma.powerup.domain.api.IReportServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReportHandler implements IReportHandler {
    private final IReportServicePort service;
    private final BootcampReportMapper mapper;

    @Override
    public Mono<Void> save(BootcampReportRequestDto request) {
        return service.save(mapper.toModel(request));
    }
}
