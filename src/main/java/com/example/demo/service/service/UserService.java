package com.example.demo.service.service;

import com.example.demo.service.dto.UpdateUserDto;
import com.example.demo.service.dto.UserDto;
import com.example.demo.service.exception.UserAlreadyExistException;
import com.example.demo.service.exception.UserIncorrectPasswordException;
import com.example.demo.service.exception.UserNotFoundException;
import com.example.demo.utils.UserRole;

import java.util.Collection;

public interface UserService {

    void registerUser(String username, String email,
                      String password) throws UserAlreadyExistException;

    UserDto updateUser(Long userId, UpdateUserDto updateUserDto)
            throws UserNotFoundException, UserIncorrectPasswordException, UserAlreadyExistException;

    UserDto updateUserRoles(Long userId, Collection<UserRole> roles) throws UserNotFoundException;
}
