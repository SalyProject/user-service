package com.saly.user.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.saly.user.IntegrationTest;
import com.saly.user.common.auth.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
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
        assertThat(savedEntity.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(10, ChronoUnit.SECONDS));
        assertThat(savedEntity.getUpdatedAt()).isCloseTo(LocalDateTime.now(), within(10, ChronoUnit.SECONDS));
    }

    @Test
    void testFindUserByEmailSuccess() {
        // SETUP
        final String searchedEmail = "searchedEmail";

        prepareUserEntity("email1", "pass");
        prepareUserEntity("email3", "pass");
        final UserEntity searchedUserEntity = prepareUserEntity(searchedEmail, "pass");

        entityManager.flush();

        // ACT
        final Optional<UserEntity> result = userDAO.findByEmail(searchedEmail);

        // VERIFY
        assertThat(result).contains(searchedUserEntity);
    }

    @Test
    void testFindUserByEmailNotFound() {
        // SETUP
        final String searchedEmail = "notExistedEmail";

        prepareUserEntity("email1", "pass");
        prepareUserEntity("email3", "pass");

        entityManager.flush();

        // ACT
        final Optional<UserEntity> result = userDAO.findByEmail(searchedEmail);

        // VERIFY
        assertThat(result).isEmpty();
    }

    private UserEntity prepareUserEntity(String email, String pass) {
        final UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setPassword(pass);

        entityManager.persist(userEntity);

        return userEntity;
    }
}