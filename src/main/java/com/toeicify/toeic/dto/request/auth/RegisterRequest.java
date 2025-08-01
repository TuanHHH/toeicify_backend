package com.toeicify.toeic.dto.request.auth;

/**
 * Created by hungpham on 7/9/2025
 */
import com.toeicify.toeic.util.constant.ValidationPatterns;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
public record RegisterRequest(
        @NotNull(message = "Full name cannot be null")
        @Pattern(regexp = ValidationPatterns.FULL_NAME_PATTERN, message = "Full name cannot contain numbers or special characters")
        String fullName,

        @NotNull(message = "Username cannot be null")
        @Pattern(regexp = ValidationPatterns.USERNAME_PATTERN, message = "Username cannot contain special characters or spaces, only -, _, ., and + are allowed")
        String username,

        @NotNull(message = "Email cannot be null")
        @Email(message = "Invalid email format")
        @Pattern(regexp = ValidationPatterns.EMAIL_PATTERN, message = "Email cannot contain special characters or spaces, only -, _, ., and + are allowed")
        String email,

        @NotNull(message = "Password cannot be null")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(regexp = ValidationPatterns.PASSWORD_PATTERN, message = "Password cannot contain spaces")
        String password,

        @NotNull(message = "Confirm Password cannot be null")
        String confirmPassword
) {
}

