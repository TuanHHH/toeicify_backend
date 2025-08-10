package com.toeicify.toeic.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toeicify.toeic.dto.response.stats.ChartPracticePointData;
import com.toeicify.toeic.dto.response.stats.UserProgressResponse;
import com.toeicify.toeic.service.UserAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hungpham on 8/10/2025
 */
@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {
    private final UserAttemptService userAttemptService;

    @GetMapping("/progress")
    public ResponseEntity<UserProgressResponse> getUserProgress(
            @RequestParam(defaultValue = "10") Integer chartLimit) throws JsonProcessingException {
        return ResponseEntity.ok(userAttemptService.getUserProgress(chartLimit));
    }
}
