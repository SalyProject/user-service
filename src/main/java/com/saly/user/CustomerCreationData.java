package com.saly.user;

import lombok.Data;

@Data
public class CustomerCreationData {
    private String name;
    private String lastName;
    private String email;
    private String password;
}
