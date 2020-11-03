package com.saly.user.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.saly.user.service.customer.Customer;
import com.saly.user.service.customer.CustomerCreationData;
import com.saly.user.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
public class CustomerControllerV1IT extends IntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testPostAndGetCustomer() {
        final CustomerCreationData data = new CustomerCreationData();
        data.setName("name");
        data.setLastName("lastName");
        data.setEmail("email");
        data.setPassword("password");

        // post
        final ResponseEntity<Customer> postResponseEntity = restTemplate.postForEntity(
                "/api/v1/customer", data, Customer.class);

        assertThat(postResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(postResponseEntity.getBody()).isNotNull();

        final UUID customerId = postResponseEntity.getBody().getId();
        assertThat(customerId).isNotNull();

        // get
        final ResponseEntity<Customer> getResponseEntity = restTemplate.getForEntity(
                "/api/v1/customer/" + customerId, Customer.class);

        assertThat(getResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponseEntity.getBody()).isNotNull();
        assertThat(getResponseEntity.getBody().getId()).isEqualTo(customerId);
    }
}
