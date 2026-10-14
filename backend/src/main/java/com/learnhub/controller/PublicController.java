package com.learnhub.controller;

import com.learnhub.entity.Role;
import com.learnhub.repository.CourseRepository;
import com.learnhub.repository.EnrollmentRepository;
import com.learnhub.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicController {
    private final UserRepository users;
    private final CourseRepository courses;
    private final EnrollmentRepository enrollments;

    public PublicController(UserRepository users, CourseRepository courses, EnrollmentRepository enrollments) {
        this.users = users;
        this.courses = courses;
        this.enrollments = enrollments;
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return Map.of(
                "students", users.countByRole(Role.STUDENT),
                "instructors", users.countByRole(Role.INSTRUCTOR),
                "courses", courses.count(),
                "enrollments", enrollments.count()
        );
    }
}
