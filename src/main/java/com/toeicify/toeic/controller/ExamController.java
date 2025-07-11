package com.toeicify.toeic.controller;

import com.toeicify.toeic.dto.request.exam.ExamRequest;
import com.toeicify.toeic.dto.response.PaginationResponse;
import com.toeicify.toeic.dto.response.exam.ExamResponse;
import com.toeicify.toeic.entity.Exam;
import com.toeicify.toeic.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hungpham on 7/10/2025
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/exams")
public class ExamController {
    private final ExamService examService;

    @PostMapping
    public ResponseEntity<ExamResponse> createExam(@Valid @RequestBody ExamRequest exam) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examService.createExam(exam));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamResponse> getExamById(@PathVariable Long id) {
        return ResponseEntity.ok(examService.getExamById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamResponse> updateExam(
            @PathVariable Long id,
            @Valid @RequestBody ExamRequest request
    ) {
        return ResponseEntity.ok(examService.updateExam(id, request));
    }

    @GetMapping
    public ResponseEntity<PaginationResponse> getAllExams(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(examService.getAllExams(page, size));
    }

    @GetMapping("/search")
    public ResponseEntity<PaginationResponse> searchExams(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(examService.searchExams(keyword, categoryId, page, size));
    }
}
