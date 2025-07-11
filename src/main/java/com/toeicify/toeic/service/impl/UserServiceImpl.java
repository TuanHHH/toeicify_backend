package com.toeicify.toeic.service.impl;

import com.toeicify.toeic.dto.request.auth.RegisterRequest;
import com.toeicify.toeic.dto.request.user.UpdatePasswordRequest;
import com.toeicify.toeic.dto.request.user.UpdateUserRequest;
import com.toeicify.toeic.dto.response.user.UserUpdateResponse;
import com.toeicify.toeic.entity.User;
import com.toeicify.toeic.exception.ResourceAlreadyExistsException;
import com.toeicify.toeic.exception.ResourceInvalidException;
import com.toeicify.toeic.exception.ResourceNotFoundException;
import com.toeicify.toeic.mapper.UserMapper;
import com.toeicify.toeic.repository.RoleRepository;
import com.toeicify.toeic.repository.UserRepository;
import com.toeicify.toeic.service.UserService;
import com.toeicify.toeic.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findByUsernameOrEmail(String identifier) {
        return userRepository.findByUsernameOrEmail(identifier).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User register(RegisterRequest request){
        if (existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("User with this email already exists.");
        }
        if (existsByUsername(request.username())) {
            throw new ResourceAlreadyExistsException("User with this username already exists.");
        }
        User newUser = User.builder()
                .fullName(request.fullName())
                .username(request.username())
                .email(request.email())
                .passwordHash(request.password()) // password is encrypted password from previous step
                .role(roleRepository.findById("GUEST").orElseThrow(() -> new ResourceNotFoundException("Default role not found")))
                .build();
        return userRepository.save(newUser);
    }
    @Override
    public User findById(Long uid) {
        return userRepository.findById(uid).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public UserUpdateResponse updateCurrentUser(UpdateUserRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.getUsername().equals(request.username()) &&
                userRepository.existsByUsername(request.username())) {
            throw new ResourceAlreadyExistsException("Username is already in use");
        }
        if (!user.getEmail().equals(request.email()) &&
                userRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Email is already in use");
        }
        user.setFullName(request.fullName());
        user.setUsername(request.username());
        user.setEmail(request.email());
        if (request.targetScore() != null) {
            if (request.isTargetScoreValid()) {
                user.setTargetScore(request.targetScore());
            } else {
                throw new ResourceInvalidException("Target score must be divisible by 5 and between 0 and 990.");
            }
        }
        user.setExamDate(request.examDate());
        userRepository.save(user);
        return userMapper.toUserUpdateResponse(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void changePassword(UpdatePasswordRequest request) {
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new ResourceInvalidException("Confirm password does not match.");
        }
        Long uid = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = findById(uid);
        if(user.getPasswordHash() == null || user.getPasswordHash().isEmpty()){
            throw new ResourceInvalidException("You account did not have password. Please login with Google or reset password.");
        }
        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Old password does not match.");
        }
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        User user = findByEmail(email);
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
