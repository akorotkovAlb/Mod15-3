package com.example.demo.service.service.impl;

import com.example.demo.data.entity.UserEntity;
import com.example.demo.data.repository.UserRepository;
import com.example.demo.service.dto.UserDto;
import com.example.demo.service.exception.UserAlreadyExistException;
import com.example.demo.service.exception.UserIncorrectPasswordException;
import com.example.demo.service.exception.UserNotFoundException;
import com.example.demo.service.mapper.UserMapper;
import com.example.demo.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private UserMapper userMapper;

    @Override
    @Transactional
    public UserDto registrationUser(String username, String password) throws UserAlreadyExistException {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            UserEntity user = new UserEntity();
            user.setUsername(username);
            user.setPassword(password);
            user.setLastUpdatedDate(LocalDate.now());
            user.setCreatedDate(LocalDate.now());

            return userMapper.toUserDto(userRepository.save(user));
        } else {
            throw new UserAlreadyExistException(username);
        }
    }

    @Override
    public UserDto login(String username, String password) throws UserNotFoundException, UserIncorrectPasswordException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (user.getPassword().equals(password)) {
            return userMapper.toUserDto(user);
        } else {
            throw new UserIncorrectPasswordException(username);
        }
    }

    @Override
    public UserDto updateUser(Long userId, String oldUsername, String oldPassword,
                              String newUsername, String newPassword)
            throws UserNotFoundException, UserIncorrectPasswordException {
        UserEntity user = userRepository.findByUsername(oldUsername)
                .orElseThrow(() -> new UserNotFoundException(oldUsername));
        if (user.getPassword().equals(oldPassword) &&
                Objects.nonNull(userId) && userId.equals(user.getId())) {
            user.setUsername(newUsername);
            user.setPassword(newPassword);
            user.setLastUpdatedDate(LocalDate.now());
            return userMapper.toUserDto(userRepository.save(user));
        } else {
            throw new UserIncorrectPasswordException(oldUsername);
        }
    }
}
