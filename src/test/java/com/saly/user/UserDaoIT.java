package com.saly.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Transactional
public class UserDaoIT extends IntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserDAO userDAO;

    @Test
    void testCreateUser() {
        // SETUP
        final String email = "email";
        final String pass = "pass";
        final Set<UserRole> roles = Set.of(UserRole.CUSTOMER);

        // ACT
        final UserEntity result = userDAO.create(email, pass, roles);

        // VERIFY
        assertNotNull(result);
        final UUID id = result.getID().getId();

        final UserEntity savedEntity = entityManager.find(UserEntity.class, id);
        assertNotNull(savedEntity);
        assertThat(savedEntity).isEqualTo(result);

        assertThat(savedEntity.getEmail()).isEqualTo(email);
        assertThat(savedEntity.getPassword()).isEqualTo(pass);
        assertThat(savedEntity.getRoles()).contains(UserRole.CUSTOMER);
    }
}