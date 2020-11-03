package com.saly.user.api;

import com.saly.user.common.exception.NotFoundException;
import com.saly.user.service.customer.Customer;
import com.saly.user.service.customer.CustomerCreationData;
import com.saly.user.service.customer.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable("id") final UUID id) {
        return customerService.getCustomerById(id)
                .orElseThrow(() -> new NotFoundException("Can't find customer by provided id"));
    }
}

