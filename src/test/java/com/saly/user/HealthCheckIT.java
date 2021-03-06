package com.saly.user;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HealthCheckIT extends IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testHealthy() {
        final ResponseEntity<ActuatorHealth> response = restTemplate.getForEntity("/actuator/health", ActuatorHealth.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo("UP");
    }

    @Test
    void testGitInfoExist() {
        final ResponseEntity<ActuatorInfo> response = restTemplate.getForEntity("/actuator/info", ActuatorInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Data
    public static class ActuatorHealth {
        private String status;
    }

    @Data
    public static class ActuatorInfo {
        private ActuatorGitInfo git;
    }

    @Data
    public static class ActuatorGitInfo {
        private String branch;
        private ActuatorGitCommitInfo commit;
    }

    @Data
    public static class ActuatorGitCommitInfo {
        private String id;
    }
}
