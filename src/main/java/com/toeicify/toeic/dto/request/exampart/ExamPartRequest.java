package com.toeicify.toeic.dto.request.exampart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Created by hungpham on 7/10/2025
 */
public record ExamPartRequest(
        @Positive(message = "Part ID must be a positive number")
        Long partId,
        @NotNull(message = "Part number is required")
        @Positive(message = "Part number must be a positive number")
        Integer partNumber,
        @NotBlank(message = "Part name is required")
        String partName,
        String description,
        @NotNull(message = "Question count is required")
        @Positive(message = "Part number must be a positive number")
        Integer questionCount
        ) {
}
