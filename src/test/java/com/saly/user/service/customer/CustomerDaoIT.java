package com.saly.user.service.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.saly.user.IntegrationTest;
import com.saly.user.service.user.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Transactional
public class CustomerDaoIT extends IntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CustomerDAO customerDAO;

    @Test
    void testCreateCustomer() {
        final UserEntity userEntity = new UserEntity();
        userEntity.setEmail("email");
        userEntity.setPassword("pass");

        testEntityManager.persist(userEntity);

        final CustomerEntity result = customerDAO.createCustomer(userEntity, "name", "lastName");
        assertNotNull(result);

        testEntityManager.flush();

        final CustomerEntity savedEntity = testEntityManager.find(CustomerEntity.class, result.getID().getId());
        assertThat(savedEntity).isEqualTo(result);

        assertThat(savedEntity.getName()).isEqualTo("name");
        assertThat(savedEntity.getLastName()).isEqualTo("lastName");
        assertThat(savedEntity.getUserEntity()).isEqualTo(userEntity);
        assertThat(savedEntity.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(10, ChronoUnit.SECONDS));
        assertThat(savedEntity.getUpdatedAt()).isCloseTo(LocalDateTime.now(), within(10, ChronoUnit.SECONDS));
    }

    @Test
    void testFindCustomerNotExist() {
        final Optional<CustomerEntity> customer = customerDAO.findById(UUID.randomUUID());
        assertThat(customer).isEmpty();
    }

    @Test
    void testFindCustomerExist() {
        final UserEntity userEntity = new UserEntity();
        userEntity.setEmail("email");
        userEntity.setPassword("pass");

        testEntityManager.persist(userEntity);

        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setName("name");
        customerEntity.setLastName("lastName");
        customerEntity.setUserEntity(userEntity);

        testEntityManager.persist(customerEntity);
        testEntityManager.flush();

        final Optional<CustomerEntity> result = customerDAO.findById(customerEntity.getID().getId());

        assertThat(result).isNotEmpty();
        final CustomerEntity findedEntity = result.get();

        assertThat(findedEntity).isEqualTo(customerEntity);
    }
}
