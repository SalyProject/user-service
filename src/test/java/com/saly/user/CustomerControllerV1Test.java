package com.saly.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saly.user.common.exception.BadRequestException;
import com.saly.user.common.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

@WebMvcTest(CustomerControllerV1.class)
class CustomerControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateCustomerSuccess() throws Exception {
        // SETUP
        final CustomerCreationData customerCreationData = new CustomerCreationData();
        customerCreationData.setName("name");
        customerCreationData.setLastName("lastName");
        customerCreationData.setEmail("email");
        customerCreationData.setPassword("password");

        final Customer customer = new Customer();
        customer.setId(UUID.randomUUID());

        when(customerService.createCustomer(customerCreationData)).thenReturn(customer);

        final MockHttpServletRequestBuilder requestBuilder = post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(customerCreationData));

        // ACT
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(customer)));
    }

    @Test
    void testCreateCustomerRuntimeException() throws Exception {
        // SETUP
        when(customerService.createCustomer(any())).thenThrow(new RuntimeException("Some exception"));

        final MockHttpServletRequestBuilder requestBuilder = post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(new CustomerCreationData()));

        final Error error = new Error(ErrorCode.INTERNAL_ERROR);

        // ACT
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(error)));
    }

    @Test
    void testCreateCustomerBadRequest() throws Exception {
        // SETUP
        when(customerService.createCustomer(any())).thenThrow(new BadRequestException(CustomerValidationMessages.EMAIL_REQUIRED));

        final MockHttpServletRequestBuilder requestBuilder = post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsBytes(new CustomerCreationData()));

        final Error error = new Error(CustomerValidationMessages.EMAIL_REQUIRED);

        // ACT
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(error)));
    }

    @Test
    void testCreateCustomerBadRequestEmptyBody() throws Exception {
        // SETUP
        final MockHttpServletRequestBuilder requestBuilder = post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        final Error error = new Error(ErrorCode.EMPTY_REQUEST_BODY);

        // ACT
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(error)));
    }
}