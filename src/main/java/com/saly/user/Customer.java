package com.saly.user;

import lombok.Data;

import java.util.UUID;

@Data
public class Customer {
    private UUID id;
    private String name;
    private String lastName;
    private String email;
}
