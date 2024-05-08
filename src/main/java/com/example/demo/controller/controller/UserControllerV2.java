package com.example.demo.controller.controller;

import com.example.demo.controller.config.jwt.JwtUtils;
import com.example.demo.controller.config.jwt.UserDetailsImpl;
import com.example.demo.controller.request.V2.auth.UserResponse;
import com.example.demo.controller.response.UpdateUserRequest;
import com.example.demo.controller.response.UpdateUserRoleRequest;
import com.example.demo.service.exception.UserAlreadyExistException;
import com.example.demo.service.exception.UserIncorrectPasswordException;
import com.example.demo.service.exception.UserNotFoundException;
import com.example.demo.service.mapper.UserMapper;
import com.example.demo.service.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/V2/users")
public class UserControllerV2 {

    @Autowired private UserService userService;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private UserMapper userMapper;

    @PutMapping("/update")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest)
            throws UserNotFoundException, UserAlreadyExistException, UserIncorrectPasswordException {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetailsImpl authentication = (UserDetailsImpl) context.getAuthentication().getPrincipal();
        return ResponseEntity.ok(userMapper.toUserResponse(
                userService.updateUser(authentication.getId(), userMapper.toUpdateUserDto(updateUserRequest))));
    }

    @PutMapping("/update/roles")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<UserResponse> updateUserRole(@Valid @RequestBody UpdateUserRoleRequest updateUserRoleRequest)
            throws UserNotFoundException {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetailsImpl authentication = (UserDetailsImpl) context.getAuthentication().getPrincipal();
        return ResponseEntity.ok(userMapper.toUserResponse(
                userService.updateUserRoles(authentication.getId(), updateUserRoleRequest.getRoles())));
    }
}
