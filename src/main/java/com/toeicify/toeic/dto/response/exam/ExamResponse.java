package com.toeicify.toeic.dto.response.exam;

/**
 * Created by hungpham on 7/10/2025
 */
import com.toeicify.toeic.dto.response.exampart.ExamPartResponse;

import java.time.Instant;
import java.util.List;

public record ExamResponse(
        Long examId,
        String examName,
        String examDescription,
        Integer totalQuestions,
        String listeningAudioUrl,
        String status,
        Instant createdAt,
        Long categoryId,
        String categoryName,
        Long createdById,
        String createdByName,
        List<ExamPartResponse> examParts
) {}

