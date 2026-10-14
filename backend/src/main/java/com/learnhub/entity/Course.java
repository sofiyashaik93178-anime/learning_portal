package com.learnhub.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    private String category;
    private String level;
    private String thumbnail;
    private Integer durationHours;
    private Double passScore;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private User instructor;

    private Instant createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
        if (passScore == null) passScore = 70.0;
    }
}
