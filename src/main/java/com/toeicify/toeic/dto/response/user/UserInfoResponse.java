package com.toeicify.toeic.dto.response.user;

import com.toeicify.toeic.entity.User;

import java.time.Instant;

/**
 * Created by hungpham on 7/7/2025
 */

public record UserInfoResponse(
        Long userId,
        String username,
        String email,
        String fullName,
        Integer targetScore,
        String roleId,
        String roleName,
        Instant examDate,
        Instant registrationDate
) {
}

