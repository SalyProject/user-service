package com.saly.user.service.user;

import com.saly.user.common.auth.UserRole;
import com.saly.user.common.domain.CommonDAO;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManager;

@Repository
public class UserDAOImpl extends CommonDAO implements UserDAO {

    public UserDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public UserEntity create(String email, String encodedPassword, Set<UserRole> roles) {
        final UserEntity userEntity = new UserEntity(email, encodedPassword, roles);

        entityManager.persist(userEntity);

        return userEntity;
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return findSingleResult(entityManager.createQuery("select u from UserEntity u where u.email = :email", UserEntity.class)
                .setParameter("email", email));
    }
}
