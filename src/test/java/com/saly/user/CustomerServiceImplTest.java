package com.saly.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.saly.user.common.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        when(userDAO.create(email, password, Set.of(UserRole.CUSTOMER))).thenReturn(userEntity);

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
    }

    @Test
    void testCreateCustomerMissingData() {
        assertThrows(BadRequestException.class, () -> customerService.createCustomer(null));
    }

    @Test
    void testCreateCustomerMissingEmail() {
        final CustomerCreationData customerCreationData = new CustomerCreationData();
        assertThrows(BadRequestException.class, () -> customerService.createCustomer(null));
    }

    @Test
    void testCreateCustomerMissingName() {
        final CustomerCreationData data = new CustomerCreationData();
        data.setEmail("email");

        assertThrows(BadRequestException.class, () -> customerService.createCustomer(null));
    }

    @Test
    void testCreateCustomerMissingPassword() {
        final CustomerCreationData data = new CustomerCreationData();
        data.setEmail("email");
        data.setName("name");

        assertThrows(BadRequestException.class, () -> customerService.createCustomer(null));
    }
}