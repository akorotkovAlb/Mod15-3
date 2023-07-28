package com.example.demo.controller.controller;

import com.example.demo.controller.request.V2.CreateUserRequest;
import com.example.demo.controller.request.V2.UpdateUserRequest;
import com.example.demo.controller.response.UserResponse;
import com.example.demo.service.dto.UserDto;
import com.example.demo.service.exception.UserAlreadyExistException;
import com.example.demo.service.exception.UserIncorrectPasswordException;
import com.example.demo.service.exception.UserNotFoundException;
import com.example.demo.service.mapper.UserMapper;
import com.example.demo.service.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@Validated
@Controller
@RequestMapping("/V2/users")
public class UserControllerV2 {

    @Autowired private UserService userService;
    @Autowired private UserMapper userMapper;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> registrationUser(@Valid @NotNull @RequestBody CreateUserRequest request)
            throws UserAlreadyExistException {
        UserDto newUser = userService.registrationUser(request.getUsername(), request.getPassword());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userMapper.toUserResponse(newUser));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> login(
            @Valid @NotNull @RequestBody CreateUserRequest request, HttpServletResponse response) throws UserNotFoundException, UserIncorrectPasswordException {
        UserDto user = userService.login(request.getUsername(), request.getPassword());
        Cookie cookie = new Cookie("userId", user.getId().toString());
        cookie.setPath("/V2/");
        response.addCookie(cookie);;
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(userMapper.toUserResponse(user));
    }

    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletResponse response) {
        response.addCookie(new Cookie("userId", null));
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(
            @CookieValue(value = "userId", required = false) Long userId,
            @Valid @NotNull @RequestBody UpdateUserRequest request)
            throws UserAlreadyExistException, UserNotFoundException, UserIncorrectPasswordException {
        UserDto newUser = userService.updateUser(userId, request.getOldUsername(), request.getOldPassword(),
                request.getNewUsername(), request.getNewPassword());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userMapper.toUserResponse(newUser));
    }
}
