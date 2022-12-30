package com.elijah.onlinebankingapp.response.customer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpResponse {
    private boolean success;
    private String message;
}
