package com.saly.user;

import static com.saly.user.CustomerValidationMessages.CUSTOMER_CREATION_DATA_REQUIRED;
import static com.saly.user.CustomerValidationMessages.EMAIL_REQUIRED;
import static com.saly.user.CustomerValidationMessages.NAME_REQUIRED;
import static com.saly.user.CustomerValidationMessages.PASSWORD_REQUIRED;
import static com.saly.user.common.exception.ValidationUtils.validateParam;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final UserDAO userDao;
    private final CustomerDAO customerDao;

    public CustomerServiceImpl(UserDAO userDao, CustomerDAO customerDao) {
        this.userDao = userDao;
        this.customerDao = customerDao;
    }

    @Override
    @Transactional
    public Customer createCustomer(final CustomerCreationData customerData) {
        validateData(customerData);

        final UserEntity userEntity = userDao.create(customerData.getEmail(), customerData.getPassword(), Set.of(UserRole.CUSTOMER));
        final CustomerEntity customerEntity = customerDao.createCustomer(userEntity, customerData.getName(), customerData.getLastName());

        return mapEntity(customerEntity);
    }

    private Customer mapEntity(final CustomerEntity customerEntity) {
        final Customer customer = new Customer();
        customer.setId(customerEntity.getID().getId());
        customer.setEmail(customerEntity.getUserEntity().getEmail());
        customer.setName(customerEntity.getName());
        customer.setLastName(customerEntity.getLastName());

        return customer;
    }

    private void validateData(final CustomerCreationData customerData) {
        validateParam(customerData, CUSTOMER_CREATION_DATA_REQUIRED, Objects::nonNull);
        validateParam(customerData.getEmail(), EMAIL_REQUIRED, Objects::nonNull);
        validateParam(customerData.getName(), NAME_REQUIRED, Objects::nonNull);
        validateParam(customerData.getPassword(), PASSWORD_REQUIRED, Objects::nonNull);
    }
}
