package com.learnhub.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Lesson {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 8000)
    private String content;

    private String videoUrl;
    private String materialUrl;
    private Integer durationMinutes;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private CourseModule module;

    @Column(name = "lesson_order")
    private Integer lessonOrder;
}
