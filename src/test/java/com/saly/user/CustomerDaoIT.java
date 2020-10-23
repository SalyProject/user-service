package com.saly.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

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

        final CustomerEntity savedEntity = testEntityManager.find(CustomerEntity.class, result.getID().getId());
        assertThat(savedEntity).isEqualTo(result);

        assertThat(savedEntity.getName()).isEqualTo("name");
        assertThat(savedEntity.getLastName()).isEqualTo("lastName");
        assertThat(savedEntity.getUserEntity()).isEqualTo(userEntity);
    }
}
