package com.saly.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CustomerControllerV1IT extends IntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testPostCustomer() {
        final CustomerCreationData data = new CustomerCreationData();
        data.setName("name");
        data.setLastName("lastName");
        data.setEmail("email");
        data.setPassword("password");

        final ResponseEntity<Customer> responseEntity = restTemplate.postForEntity(
                "/api/v1/customer",
                data,
                Customer.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getId()).isNotNull();
    }
}
