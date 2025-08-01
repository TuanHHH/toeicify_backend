package com.toeicify.toeic.dto.response.user;

import com.toeicify.toeic.entity.User;

import java.time.Instant;

public record AdminUserResponse(
        Long userId,
        String username,
        String email,
        String fullName,
        String roleId,
        String roleName,
        Boolean isActive,
        Instant registrationDate,
        String lockReason
) {
}