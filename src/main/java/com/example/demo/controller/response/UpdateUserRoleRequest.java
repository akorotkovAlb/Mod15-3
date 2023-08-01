package com.example.demo.controller.response;

import com.example.demo.utils.UserRole;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UpdateUserRoleRequest {

    @NotEmpty
    Set<UserRole> roles = new HashSet<>();
}
