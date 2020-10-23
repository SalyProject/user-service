package com.saly.user;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

    @PersistenceContext
    private final EntityManager entityManager;

    public CustomerDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public CustomerEntity createCustomer(UserEntity userEntity, String name, String lastName) {
        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setUserEntity(userEntity);
        customerEntity.setName(name);
        customerEntity.setLastName(lastName);

        entityManager.persist(customerEntity);

        return customerEntity;
    }
}
