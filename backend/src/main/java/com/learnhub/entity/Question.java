package com.learnhub.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String questionText;

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
}
