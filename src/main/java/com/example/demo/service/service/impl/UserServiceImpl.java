package com.example.demo.service.service.impl;

import com.example.demo.controller.config.jwt.UserDetailsImpl;
import com.example.demo.data.entity.RoleEntity;
import com.example.demo.data.entity.UserEntity;
import com.example.demo.data.repository.RoleRepository;
import com.example.demo.data.repository.UserRepository;
import com.example.demo.service.dto.UpdateUserDto;
import com.example.demo.service.dto.UserDto;
import com.example.demo.service.exception.UserAlreadyExistException;
import com.example.demo.service.exception.UserIncorrectPasswordException;
import com.example.demo.service.exception.UserNotFoundException;
import com.example.demo.service.mapper.UserMapper;
import com.example.demo.service.service.UserService;
import com.example.demo.utils.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder encoder;
    @Autowired private UserMapper userMapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

    @Override
    @Transactional
    public void registerUser(String username, String email,
                             String password) throws UserAlreadyExistException {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistException(username, email);
        }
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistException(username, email);
        }

        UserEntity user = new UserEntity(username, email, encoder.encode(password));
        Set<RoleEntity> roleEntities = roleRepository.findByNames(Collections.singleton(UserRole.USER));
        user.setRoles(roleEntities);
        user.setLastUpdatedDate(LocalDate.now());
        user.setCreatedDate(LocalDate.now());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UpdateUserDto updateUserDto)
            throws UserNotFoundException, UserIncorrectPasswordException, UserAlreadyExistException {
        UserEntity user = userRepository.findByUsername(updateUserDto.getOldUsername())
                .orElseThrow(() -> new UserNotFoundException(updateUserDto.getOldUsername()));
        if (userRepository.existsByEmail(updateUserDto.getNewEmail())) {
            throw new UserAlreadyExistException(updateUserDto.getOldUsername(), updateUserDto.getNewEmail());
        }
        if (user.getPassword().equals(encoder.encode(updateUserDto.getOldPassword())) &&
                Objects.nonNull(userId) && userId.equals(user.getId())) {
            user.setUsername(updateUserDto.getNewUsername());
            user.setPassword(encoder.encode(updateUserDto.getNewPassword()));
            user.setLastUpdatedDate(LocalDate.now());
            return userMapper.toUserDto(userRepository.save(user));
        } else {
            throw new UserIncorrectPasswordException(updateUserDto.getOldUsername());
        }
    }

    @Override
    @Transactional
    public UserDto updateUserRoles(Long userId, Collection<UserRole> roles) throws UserNotFoundException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Set<RoleEntity> roleEntities = roleRepository.findByNames(roles);
        user.setRoles(roleEntities);
        user.setLastUpdatedDate(LocalDate.now());
        return userMapper.toUserDto(userRepository.save(user));
    }
}
