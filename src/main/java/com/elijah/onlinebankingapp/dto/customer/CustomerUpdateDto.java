package com.elijah.onlinebankingapp.dto.customer;


import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerUpdateDto {
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String password;
    private String occupation;
    private LocalDate dateOfBirth;
    private String maritalStatus;
}
