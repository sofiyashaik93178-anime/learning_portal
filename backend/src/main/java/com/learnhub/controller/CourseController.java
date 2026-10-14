package com.learnhub.controller;

import com.learnhub.entity.User;
import com.learnhub.service.LearningService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final LearningService learning;

    public CourseController(LearningService learning) {
        this.learning = learning;
    }

    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(learning.listCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(learning.courseDetail(id, user));
    }

    @PostMapping("/{id}/enroll")
    public ResponseEntity<?> enroll(@PathVariable long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(learning.enroll(user, id));
    }
}
