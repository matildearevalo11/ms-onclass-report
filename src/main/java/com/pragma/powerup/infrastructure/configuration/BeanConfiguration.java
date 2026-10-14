package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.IReportServicePort;
import com.pragma.powerup.domain.spi.IReportPersistencePort;
import com.pragma.powerup.domain.usecase.ReportUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    IReportServicePort reportServicePort(IReportPersistencePort persistence) {
        return new ReportUseCase(persistence);
    }
}
