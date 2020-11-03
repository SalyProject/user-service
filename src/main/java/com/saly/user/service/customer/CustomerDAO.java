package com.saly.user.service.customer;

import com.saly.user.service.user.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface CustomerDAO {
    CustomerEntity createCustomer(UserEntity userEntity, String name, String lastName);

    Optional<CustomerEntity> findById(UUID id);
}
