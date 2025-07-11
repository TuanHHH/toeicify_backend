package com.toeicify.toeic.service.impl;

import com.toeicify.toeic.dto.request.exam.ExamRequest;
import com.toeicify.toeic.dto.request.exampart.ExamPartRequest;
import com.toeicify.toeic.dto.response.PaginationResponse;
import com.toeicify.toeic.dto.response.exam.ExamResponse;
import com.toeicify.toeic.entity.Exam;
import com.toeicify.toeic.entity.ExamCategory;
import com.toeicify.toeic.entity.ExamPart;
import com.toeicify.toeic.entity.User;
import com.toeicify.toeic.exception.ResourceAlreadyExistsException;
import com.toeicify.toeic.exception.ResourceInvalidException;
import com.toeicify.toeic.mapper.ExamMapper;
import com.toeicify.toeic.repository.ExamRepository;
import com.toeicify.toeic.service.ExamCategoryService;
import com.toeicify.toeic.service.ExamService;
import com.toeicify.toeic.service.UserService;
import com.toeicify.toeic.util.SecurityUtil;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by hungpham on 7/10/2025
 */
@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final ExamMapper examMapper;
    private final ExamCategoryService examCategoryService;
    private final UserService userService;

    @Override
    @Transactional
    public ExamResponse createExam(ExamRequest request) {
        Long uid = SecurityUtil.getCurrentUserId();
        User user = userService.findById(uid);
        ExamCategory category = examCategoryService.findExamCategoryById(request.categoryId());
        if (examRepository.existsByExamName(request.examName())) {
            throw new ResourceAlreadyExistsException("Exam name already exists");
        }
        Exam exam = Exam.builder()
                .examName(request.examName())
                .examDescription(request.examDescription())
                .totalQuestions(request.totalQuestions())
                .listeningAudioUrl(request.listeningAudioUrl())
                .examCategory(category)
                .createdBy(user)
                .build();

        List<ExamPart> parts = request.examParts().stream()
                .map(p -> ExamPart.builder()
                        .exam(exam)
                        .partNumber(p.partNumber())
                        .partName(p.partName())
                        .description(p.description())
                        .questionCount(p.questionCount())
                        .build()
                ).toList();

        exam.setExamParts(parts);

        Exam savedExam = examRepository.save(exam);
        return examMapper.toExamResponse(savedExam);
    }

    @Override
    @Transactional(readOnly = true)
    public ExamResponse getExamById(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        return examMapper.toExamResponse(exam);
    }

    @Transactional(readOnly = true)
    @Override
    public PaginationResponse getAllExams(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Exam> examPage = examRepository.findAll(pageable);
        Page<ExamResponse> pageResponse = examPage.map(examMapper::toExamResponse);
        return PaginationResponse.from(pageResponse, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public PaginationResponse searchExams(String keyword, Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Exam> exams;
        if (keyword != null && categoryId != null) {
            exams = examRepository.findByExamNameContainingIgnoreCaseAndExamCategory_CategoryId(keyword, categoryId, pageable);
        } else if (keyword != null) {
            exams = examRepository.findByExamNameContainingIgnoreCase(keyword, pageable);
        } else if (categoryId != null) {
            exams = examRepository.findByExamCategory_CategoryId(categoryId, pageable);
        } else {
            exams = examRepository.findAll(pageable);
        }
        Page<ExamResponse> pageResponse = exams.map(examMapper::toExamResponse);
        return PaginationResponse.from(pageResponse, pageable);
    }
    @Transactional
    @Override
    public ExamResponse updateExam(Long id, ExamRequest request) {
        Exam exam = examRepository.findWithPartsByExamId(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        ExamCategory category = examCategoryService.findExamCategoryById(request.categoryId());

        exam.setExamName(request.examName());
        exam.setExamDescription(request.examDescription());
        exam.setTotalQuestions(request.totalQuestions());
        exam.setListeningAudioUrl(request.listeningAudioUrl());
        exam.setExamCategory(category);

        Map<Long, ExamPart> existingParts = exam.getExamParts().stream()
                .filter(p -> p.getPartId() != null)
                .collect(Collectors.toMap(ExamPart::getPartId, p -> p));

        // ID của các part từ request
        Set<Long> incomingPartIds = request.examParts().stream()
                .map(ExamPartRequest::partId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Xóa các part không nằm trong request
        exam.getExamParts().removeIf(part -> part.getPartId() != null && !incomingPartIds.contains(part.getPartId()));

        for (ExamPartRequest dto : request.examParts()) {
            if (dto.partId() != null) {
                // Sửa part cũ
                ExamPart part = existingParts.get(dto.partId());
                if (part != null) {
                    part.setPartNumber(dto.partNumber());
                    part.setPartName(dto.partName());
                    part.setDescription(dto.description());
                    part.setQuestionCount(dto.questionCount());
                } else {
                    throw new ResourceInvalidException("Part not found: ID " + dto.partId());
                }
            } else {
                // Thêm part mới
                ExamPart newPart = ExamPart.builder()
                        .exam(exam)
                        .partNumber(dto.partNumber())
                        .partName(dto.partName())
                        .description(dto.description())
                        .questionCount(dto.questionCount())
                        .build();
                exam.getExamParts().add(newPart);
            }
        }

        Exam updated = examRepository.save(exam);
        return examMapper.toExamResponse(updated);
    }
}
