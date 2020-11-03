package com.saly.user.service.customer;

import static com.saly.user.common.exception.ValidationUtils.validateParam;

import com.saly.user.service.user.UserDAO;
import com.saly.user.service.user.UserEntity;
import com.saly.user.service.user.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final UserDAO userDao;
    private final CustomerDAO customerDao;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(UserDAO userDao, CustomerDAO customerDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.customerDao = customerDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Customer createCustomer(final CustomerCreationData customerData) {
        validateData(customerData);

        final UserEntity userEntity = userDao.create(customerData.getEmail(), passwordEncoder.encode(customerData.getPassword()), Set.of(
                UserRole.CUSTOMER));
        final CustomerEntity customerEntity = customerDao.createCustomer(userEntity, customerData.getName(), customerData.getLastName());

        return mapEntity(customerEntity);
    }

    @Override
    @Transactional
    public Optional<Customer> getCustomerById(final UUID id) {
        return customerDao.findById(id).map(this::mapEntity);
    }

    private Customer mapEntity(final CustomerEntity customerEntity) {
        final Customer customer = new Customer();
        customer.setId(customerEntity.getID().getId());
        customer.setEmail(customerEntity.getUserEntity().getEmail());
        customer.setName(customerEntity.getName());
        customer.setLastName(customerEntity.getLastName());
        customer.setCreatedAt(customerEntity.getCreatedAt());
        customer.setUpdatedAt(customerEntity.getUpdatedAt());

        return customer;
    }

    private void validateData(final CustomerCreationData customerData) {
        validateParam(customerData, "Data is missing", Objects::nonNull);
        validateParam(customerData.getEmail(), "Email is required", Objects::nonNull);
        validateParam(customerData.getName(), "Name is required", Objects::nonNull);
        validateParam(customerData.getPassword(), "Password is required", Objects::nonNull);
    }
}
