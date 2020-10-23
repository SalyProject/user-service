package com.saly.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerControllerV1 {

    private final CustomerService customerService;

    public CustomerControllerV1(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public Customer createCustomer(@RequestBody final CustomerCreationData customerData) {
        return customerService.createCustomer(customerData);
    }
}

