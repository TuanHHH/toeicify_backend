package com.toeicify.toeic.service.impl;

import com.toeicify.toeic.dto.request.examcategory.ExamCategoryRequest;
import com.toeicify.toeic.dto.response.PaginationResponse;
import com.toeicify.toeic.dto.response.examcategory.ExamCategoryResponse;
import com.toeicify.toeic.entity.ExamCategory;
import com.toeicify.toeic.exception.ResourceAlreadyExistsException;
import com.toeicify.toeic.exception.ResourceNotFoundException;
import com.toeicify.toeic.mapper.ExamCategoryMapper;
import com.toeicify.toeic.repository.ExamCategoryRepository;
import com.toeicify.toeic.service.ExamCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by hungpham on 7/10/2025
 */
@Service
@RequiredArgsConstructor
public class ExamCategoryServiceImpl implements ExamCategoryService {
    private final ExamCategoryRepository examCategoryRepository;
    private final ExamCategoryMapper examCategoryMapper;

    @Override

    public ExamCategoryResponse createExamCategory(ExamCategoryRequest examCategory) {
        if (examCategoryRepository.existsByCategoryName(examCategory.categoryName())) {
            throw new ResourceAlreadyExistsException("Category already exists");
        }
        ExamCategory examCategoryEntity = examCategoryMapper.toExamCategory(examCategory);
        return examCategoryMapper.toResponse(examCategoryRepository.save(examCategoryEntity));
    }

    @Override
    public ExamCategoryResponse updateExamCategory(Long id, ExamCategoryRequest examCategory) {
        ExamCategory category = findExamCategoryById(id);
        if (examCategoryRepository.existsByCategoryNameAndCategoryIdNot(examCategory.categoryName(), id)) {
            throw new ResourceAlreadyExistsException("Category name must be unique");
        }
        category.setCategoryName(examCategory.categoryName());
        category.setDescription(examCategory.description());
        return examCategoryMapper.toResponse(examCategoryRepository.save(category));
    }

    @Override
    public void deleteExamCategory(Long id) {

    }

    @Override
    public PaginationResponse getAllExamCategories(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<ExamCategory> pageResult = examCategoryRepository.findAll(pageable);
        Page<ExamCategoryResponse> responsePage = pageResult.map(examCategoryMapper::toResponse);
        return PaginationResponse.from(responsePage, pageable);
    }

    @Override
    public ExamCategoryResponse getExamCategoryById(Long id) {
        return examCategoryMapper.toResponse(findExamCategoryById(id));
    }

    @Override
    public ExamCategory findExamCategoryById(Long id) {
        return examCategoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Exam category not found"));
    }
}
