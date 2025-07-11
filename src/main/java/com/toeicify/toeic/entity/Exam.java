package com.toeicify.toeic.entity;

import com.toeicify.toeic.util.enums.ExamStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

/**
 * Created by hungpham on 7/10/2025
 */
@Entity
@Table(name = "exams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    private Long examId;

    @Column(name = "exam_name", unique = true,nullable = false)
    private String examName;

    @Column(name = "exam_description", nullable = false)
    private String examDescription;

    @Column(name = "total_questions")
    private Integer totalQuestions;

    @Column(name = "listening_audio_url")
    private String listeningAudioUrl;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ExamStatus status = ExamStatus.PENDING;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamPart> examParts;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "user_id", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private ExamCategory examCategory;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();
    }
}
