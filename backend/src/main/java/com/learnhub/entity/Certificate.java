package com.learnhub.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Certificate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(unique = true)
    private String certificateCode;

    private Instant issuedAt;

    @PrePersist
    void onCreate() {
        if (issuedAt == null) issuedAt = Instant.now();
    }
}
