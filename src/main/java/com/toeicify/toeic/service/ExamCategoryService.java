package com.toeicify.toeic.service;

import com.toeicify.toeic.dto.request.examcategory.ExamCategoryRequest;
import com.toeicify.toeic.dto.response.PaginationResponse;
import com.toeicify.toeic.dto.response.examcategory.ExamCategoryResponse;
import com.toeicify.toeic.entity.ExamCategory;
import org.springframework.data.domain.Page;

/**
 * Created by hungpham on 7/10/2025
 */
public interface ExamCategoryService {
    ExamCategoryResponse createExamCategory(ExamCategoryRequest examCategory);
    ExamCategoryResponse updateExamCategory(Long id, ExamCategoryRequest examCategory);
    void deleteExamCategory(Long id);
    PaginationResponse getAllExamCategories(int page, int pageSize);
    ExamCategoryResponse getExamCategoryById(Long id);

    ExamCategory findExamCategoryById(Long id);
}
