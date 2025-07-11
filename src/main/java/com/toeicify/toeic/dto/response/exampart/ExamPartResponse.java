package com.toeicify.toeic.dto.response.exampart;

/**
 * Created by hungpham on 7/10/2025
 */
public record ExamPartResponse(
        Long partId,
        Integer partNumber,
        String partName,
        String description,
        Integer questionCount
) {}
