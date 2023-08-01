package com.example.demo.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDto {

    private String oldUsername;
    private String oldPassword;
    private String oldEmail;
    private String newUsername;
    private String newPassword;
    private String newEmail;
}
