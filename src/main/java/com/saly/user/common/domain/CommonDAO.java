package com.saly.user.common.domain;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public abstract class CommonDAO {

    @PersistenceContext
    protected final EntityManager entityManager;

    protected CommonDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    protected <T> Optional<T> findSingleResult(TypedQuery<T> query) {
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
