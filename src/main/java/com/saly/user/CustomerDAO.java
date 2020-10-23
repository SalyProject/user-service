package com.saly.user;

public interface CustomerDAO {
    CustomerEntity createCustomer(UserEntity userEntity, String name, String lastName);
}
