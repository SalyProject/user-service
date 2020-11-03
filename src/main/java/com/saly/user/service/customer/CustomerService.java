package com.saly.user.service.customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    Customer createCustomer(CustomerCreationData customerData);

    Optional<Customer> getCustomerById(UUID id);
}
