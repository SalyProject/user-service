package com.saly.user.service.customer;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Customer {
    private UUID id;
    private String name;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
