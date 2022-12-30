package com.elijah.onlinebankingapp.dto.customer;

import lombok.Data;

@Data
public class SignInDto {
    private String email;
    private String password;
}
