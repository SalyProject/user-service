package com.saly.user.service.user;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private final EntityManager entityManager;

    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public UserEntity create(String email, String encodedPassword, Set<UserRole> roles) {
        final UserEntity userEntity = new UserEntity(email, encodedPassword, roles);

        entityManager.persist(userEntity);

        return userEntity;
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return Optional.ofNullable(entityManager.createQuery("select u from UserEntity u where u.email = :email", UserEntity.class)
                .setParameter("email", email).getSingleResult());
    }
}
