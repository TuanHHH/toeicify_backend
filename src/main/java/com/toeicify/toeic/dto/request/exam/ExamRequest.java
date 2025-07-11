package com.toeicify.toeic.dto.request.exam;

/**
 * Created by hungpham on 7/10/2025
 */
import com.toeicify.toeic.dto.request.exampart.ExamPartRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record ExamRequest(
        @NotBlank String examName,
        @NotBlank String examDescription,
        @NotNull @Min(1) Integer totalQuestions,
        @NotBlank String listeningAudioUrl,
        @NotNull Long categoryId,
        @NotEmpty List<@Valid ExamPartRequest> examParts
) {}

