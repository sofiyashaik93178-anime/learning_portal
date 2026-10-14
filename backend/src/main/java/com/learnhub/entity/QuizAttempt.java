package com.learnhub.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QuizAttempt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    private Double score;
    private Boolean passed;
    private Instant attemptedAt;

    @PrePersist
    void onCreate() {
        if (attemptedAt == null) attemptedAt = Instant.now();
    }
}
