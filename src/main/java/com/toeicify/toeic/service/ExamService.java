package com.toeicify.toeic.service;

import com.toeicify.toeic.dto.request.exam.ExamRequest;
import com.toeicify.toeic.dto.response.PaginationResponse;
import com.toeicify.toeic.dto.response.exam.ExamResponse;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by hungpham on 7/10/2025
 */
public interface ExamService {
    ExamResponse createExam(ExamRequest request);

    @Transactional(readOnly = true)
    ExamResponse getExamById(Long id);

    @Transactional(readOnly = true)
    PaginationResponse getAllExams(int page, int size);

    @Transactional(readOnly = true)
    PaginationResponse searchExams(String keyword, Long categoryId, int page, int size);

    @Transactional
    ExamResponse updateExam(Long id, ExamRequest request);
}
