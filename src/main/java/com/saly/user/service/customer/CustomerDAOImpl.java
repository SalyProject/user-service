package com.saly.user.service.customer;

import com.saly.user.common.domain.CommonDAO;
import com.saly.user.service.user.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerDAOImpl extends CommonDAO implements CustomerDAO {

    public CustomerDAOImpl(EntityManager entityManager) {
        super(entityManager);
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

    @Override
    public Optional<CustomerEntity> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(CustomerEntity.class, id));
    }
}
