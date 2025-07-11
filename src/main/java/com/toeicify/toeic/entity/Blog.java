package com.toeicify.toeic.entity;

import com.toeicify.toeic.util.enums.BlogStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Created by hungpham on 7/10/2025
 */
@Entity
@Table(name = "blogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private Long blogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "published_date", nullable = false)
    private Instant publishedDate;

    @Column(name = "last_updated", nullable = false)
    private Instant lastUpdated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BlogStatus status;

    @PrePersist
    public void handleBeforeCreate() {
        this.publishedDate = Instant.now();
    }
}
