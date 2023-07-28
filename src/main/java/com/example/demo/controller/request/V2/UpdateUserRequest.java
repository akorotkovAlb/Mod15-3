package com.example.demo.controller.request.V2;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    @Size(min = 3, max = 100)
    private String oldUsername;

    @Size(min = 3)
    private String oldPassword;

    @Size(min = 3, max = 100)
    private String newUsername;

    @Size(min = 3)
    private String newPassword;
}
