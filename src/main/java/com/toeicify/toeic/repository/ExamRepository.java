package com.toeicify.toeic.repository;

import com.toeicify.toeic.entity.Exam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by hungpham on 7/10/2025
 */
@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    Page<Exam> findByExamNameContainingIgnoreCaseAndExamCategory_CategoryId(String keyword, Long categoryId,
                                                                            Pageable pageable);
    Page<Exam> findByExamNameContainingIgnoreCase(String keyword,
                                                  Pageable pageable);

    Page<Exam> findByExamCategory_CategoryId(Long categoryId,
                                             Pageable pageable);
    boolean existsByExamName(String name);

    @EntityGraph(attributePaths = "examParts")
    Optional<Exam> findWithPartsByExamId(Long examId);

}
