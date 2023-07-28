package com.example.demo.controller.request.V2;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    @Size(min = 3, max = 100)
    private String username;

    @Size(min = 3)
    private String password;
}
