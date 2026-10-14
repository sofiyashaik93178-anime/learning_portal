package com.learnhub.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Enrollment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private Instant enrolledAt;
    private String status;

    @PrePersist
    void onCreate() {
        if (enrolledAt == null) enrolledAt = Instant.now();
        if (status == null) status = "ACTIVE";
    }
}
