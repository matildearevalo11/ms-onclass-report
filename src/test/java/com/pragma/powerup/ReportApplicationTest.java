package com.pragma.powerup;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import com.pragma.powerup.domain.model.BootcampReport;
import com.pragma.powerup.domain.spi.IReportPersistencePort;
import org.mockito.ArgumentCaptor;
import org.springframework.core.io.ClassPathResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@AutoConfigureWebTestClient
class ReportApplicationTest {
    @Autowired
    private WebTestClient client;
    @MockitoBean
    private IReportPersistencePort persistence;

    @Test
    void storesCompleteBootcampSnapshotOnlyWithInternalToken() {
        org.mockito.Mockito.when(persistence.save(org.mockito.ArgumentMatchers.any()))
                .thenReturn(reactor.core.publisher.Mono.empty());
        Map<String, Object> report = Map.of(
                "bootcampId", 7, "name", "Backend", "description", "Services",
                "launchDate", "2026-11-01", "durationDays", 90,
                "capacities", List.of(Map.of("id", 1, "name", "API",
                        "technologies", List.of(Map.of("id", 3, "name", "Java")))),
                "capacityCount", 1, "technologyCount", 1, "personCount", 0);

        client.post().uri("/internal/bootcamp-reports")
                .header("X-Internal-Token", "local-dev-report-secret-change-me")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(report)
                .exchange().expectStatus().isNoContent();

        ArgumentCaptor<BootcampReport> saved = ArgumentCaptor.forClass(BootcampReport.class);
        org.mockito.Mockito.verify(persistence).save(saved.capture());
        org.assertj.core.api.Assertions.assertThat(saved.getValue().getBootcampId()).isEqualTo(7L);
        org.assertj.core.api.Assertions.assertThat(saved.getValue().getCapacities().getFirst()
                .getTechnologies().getFirst().getName()).isEqualTo("Java");
        org.assertj.core.api.Assertions.assertThat(saved.getValue().getPersonCount()).isZero();

        client.post().uri("/internal/bootcamp-reports")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(report)
                .exchange().expectStatus().isUnauthorized();
        client.post().uri("/internal/bootcamp-reports")
                .header("X-Internal-Token", "wrong")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(report)
                .exchange().expectStatus().isUnauthorized()
                .expectBody().jsonPath("$.errors.message")
                .isEqualTo(com.pragma.powerup.domain.exception.ExceptionMessages.AUTHENTICATION_REQUIRED.getMessage());
        org.mockito.Mockito.verifyNoMoreInteractions(persistence);
    }

    @Test
    void rejectsInvalidReportBody() {
        client.post().uri("/internal/bootcamp-reports")
                .header("X-Internal-Token", "local-dev-report-secret-change-me")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("bootcampId", -1, "name", ""))
                .exchange().expectStatus().isBadRequest()
                .expectBody().jsonPath("$.errors.message").isNotEmpty();
        client.post().uri("/internal/bootcamp-reports")
                .header("X-Internal-Token", "local-dev-report-secret-change-me")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{bad json")
                .exchange().expectStatus().isBadRequest()
                .expectBody().jsonPath("$.errors.message").isEqualTo(com.pragma.powerup.domain.exception.ExceptionMessages.INVALID_REQUEST.getMessage());
    }

    @Test
    void updatesPersonCountOnlyWithInternalToken() {
        org.mockito.Mockito.when(persistence.updatePersonCount(7L, 3))
                .thenReturn(reactor.core.publisher.Mono.just(true));
        org.mockito.Mockito.when(persistence.updatePersonCount(8L, 3))
                .thenReturn(reactor.core.publisher.Mono.just(false));

        patchPersonCount("/internal/bootcamp-reports/7/person-count", Map.of("personCount", 3), true)
                .expectStatus().isNoContent();
        patchPersonCount("/internal/bootcamp-reports/8/person-count", Map.of("personCount", 3), true)
                .expectStatus().isNotFound().expectBody().jsonPath("$.errors.message")
                .isEqualTo(com.pragma.powerup.domain.exception.ExceptionMessages.REPORT_NOT_FOUND.getMessage());
        patchPersonCount("/internal/bootcamp-reports/7/person-count", Map.of("personCount", -1), true)
                .expectStatus().isBadRequest();
        patchPersonCount("/internal/bootcamp-reports/7/person-count", Map.of("personCount", 3), false)
                .expectStatus().isUnauthorized();
    }

    private org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec patchPersonCount(
            String uri, Map<String, Object> body, boolean withToken) {
        var request = client.patch().uri(uri).contentType(MediaType.APPLICATION_JSON);
        if (withToken) {
            request = request.header("X-Internal-Token", "local-dev-report-secret-change-me");
        }
        return request.bodyValue(body).exchange();
    }

    @Test
    void reportsMongoFailureAsServiceUnavailable() {
        org.mockito.Mockito.when(persistence.save(org.mockito.ArgumentMatchers.any()))
                .thenReturn(reactor.core.publisher.Mono.error(
                        new org.springframework.dao.DataAccessResourceFailureException("Mongo unavailable")));
        client.post().uri("/internal/bootcamp-reports")
                .header("X-Internal-Token", "local-dev-report-secret-change-me")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("bootcampId", 7, "name", "Backend", "description", "Services",
                        "launchDate", "2026-11-01", "durationDays", 90,
                        "capacities", List.of(), "capacityCount", 0, "technologyCount", 0,
                        "personCount", 0))
                .exchange().expectStatus().isEqualTo(503)
                .expectBody().jsonPath("$.errors.message")
                .isEqualTo(com.pragma.powerup.domain.exception.ExceptionMessages.DATABASE_UNAVAILABLE.getMessage());
    }

    @Test
    void servesStaticOpenApiThroughSwaggerUi() throws IOException {
        String contract = new ClassPathResource("openapi.yaml")
                .getContentAsString(StandardCharsets.UTF_8);
        client.get().uri("/openapi.yaml").exchange().expectStatus().isOk()
                .expectBody(String.class).isEqualTo(contract);
        client.get().uri("/swagger-ui.html").exchange().expectStatus().is3xxRedirection();
        client.get().uri("/webjars/swagger-ui/index.html").exchange().expectStatus().isOk();
        client.get().uri("/v3/api-docs/swagger-config").exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.url").isEqualTo("/api/v1/openapi.yaml");
        client.get().uri("/v3/api-docs").exchange().expectStatus().isNotFound();
    }
}
