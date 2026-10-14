package com.learnhub.controller;

import com.learnhub.entity.User;
import com.learnhub.repository.CertificateRepository;
import com.learnhub.service.LearningService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class StudentController {
    private final LearningService learning;
    private final CertificateRepository certificates;

    public StudentController(LearningService learning, CertificateRepository certificates) {
        this.learning = learning;
        this.certificates = certificates;
    }

    @GetMapping("/student/dashboard")
    public ResponseEntity<?> dashboard(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(learning.studentDashboard(user));
    }

    @GetMapping("/lessons/{id}")
    public ResponseEntity<?> lesson(@PathVariable long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(learning.lessonForStudent(user, id));
    }

    @PostMapping("/lessons/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(learning.completeLesson(user, id));
    }

    @PostMapping("/quizzes/{id}/submit")
    public ResponseEntity<?> submit(@PathVariable long id, @AuthenticationPrincipal User user, @RequestBody Map<String, String> answers) {
        return ResponseEntity.ok(learning.submitQuiz(user, id, answers));
    }

    @GetMapping("/certificates")
    public ResponseEntity<?> myCerts(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(certificates.findByStudentId(user.getId()).stream().map(learning::certView).toList());
    }

    @GetMapping("/certificates/verify/{code}")
    public ResponseEntity<?> verify(@PathVariable String code) {
        return certificates.findByCertificateCode(code)
                .map(c -> ResponseEntity.ok(learning.certView(c)))
                .orElse(ResponseEntity.notFound().build());
    }
}
