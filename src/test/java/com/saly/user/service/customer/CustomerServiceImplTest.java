package com.saly.user.service.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.saly.user.service.user.UserDAO;
import com.saly.user.service.user.UserEntity;
import com.saly.user.common.auth.UserRole;
import com.saly.user.common.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ExtendWith({MockitoExtension.class})
class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private UserDAO userDAO;
    @Mock
    private CustomerDAO customerDAO;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testCreateCustomerSuccess() {
        // SETUP
        String email = "email";
        String name = "name";
        String lastName = "lastName";
        String password = "password";

        final CustomerCreationData data = new CustomerCreationData();
        data.setEmail(email);
        data.setName(name);
        data.setLastName(lastName);
        data.setPassword(password);

        final UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setEmail(email);

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userDAO.create(email, encodedPassword, Set.of(UserRole.CUSTOMER))).thenReturn(userEntity);

        final UUID customerId = UUID.randomUUID();
        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customerId);
        customerEntity.setName(name);
        customerEntity.setLastName(lastName);
        customerEntity.setUserEntity(userEntity);

        when(customerDAO.createCustomer(userEntity, name, lastName)).thenReturn(customerEntity);

        // ACT
        final Customer customer = customerService.createCustomer(data);

        //VERIFY
        assertThat(customer.getId()).isEqualTo(customerId);
        assertThat(customer.getEmail()).isEqualTo(email);
        assertThat(customer.getName()).isEqualTo(name);
        assertThat(customer.getLastName()).isEqualTo(lastName);
        assertThat(customer.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(10, ChronoUnit.SECONDS));
        assertThat(customer.getUpdatedAt()).isCloseTo(LocalDateTime.now(), within(10, ChronoUnit.SECONDS));
    }

    @Test
    void testCreateCustomerMissingData() {
        assertThrows(BadRequestException.class, () -> customerService.createCustomer(null));
    }

    @Test
    void testCreateCustomerMissingEmail() {
        final CustomerCreationData data = new CustomerCreationData();
        assertThrows(BadRequestException.class, () -> customerService.createCustomer(data));
    }

    @Test
    void testCreateCustomerMissingName() {
        final CustomerCreationData data = new CustomerCreationData();
        data.setEmail("email");

        assertThrows(BadRequestException.class, () -> customerService.createCustomer(data));
    }

    @Test
    void testCreateCustomerMissingPassword() {
        final CustomerCreationData data = new CustomerCreationData();
        data.setEmail("email");
        data.setName("name");

        assertThrows(BadRequestException.class, () -> customerService.createCustomer(data));
    }

    @Test
    void testGetCustomerByIdExist() {
        // SETUP
        final UUID customerId = UUID.randomUUID();
        String email = "email";
        String name = "name";
        String lastName = "lastName";

        final UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setEmail(email);

        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customerId);
        customerEntity.setId(customerId);
        customerEntity.setName(name);
        customerEntity.setLastName(lastName);
        customerEntity.setUserEntity(userEntity);

        when(customerDAO.findById(customerId)).thenReturn(Optional.of(customerEntity));

        // ACT
        final Optional<Customer> result = customerService.getCustomerById(customerId);

        // VERIFY
        assertThat(result).isNotEmpty();

        final Customer customer = result.get();
        assertThat(customer.getId()).isEqualTo(customerId);
        assertThat(customer.getEmail()).isEqualTo(email);
        assertThat(customer.getName()).isEqualTo(name);
        assertThat(customer.getLastName()).isEqualTo(lastName);
    }

    @Test
    void testGetCustomerByEmpty() {
        // SETUP
        final UUID customerId = UUID.randomUUID();
        when(customerDAO.findById(customerId)).thenReturn(Optional.empty());

        // ACT
        final Optional<Customer> result = customerService.getCustomerById(customerId);

        // VERIFY
        assertThat(result).isEmpty();
    }
}