package com.learnhub.controller;

import com.learnhub.entity.Role;
import com.learnhub.entity.User;
import com.learnhub.repository.CourseRepository;
import com.learnhub.repository.EnrollmentRepository;
import com.learnhub.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserRepository users;
    private final CourseRepository courses;
    private final EnrollmentRepository enrollments;

    public AdminController(UserRepository users, CourseRepository courses, EnrollmentRepository enrollments) {
        this.users = users;
        this.courses = courses;
        this.enrollments = enrollments;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return Map.of(
                "totalUsers", users.count(),
                "students", users.countByRole(Role.STUDENT),
                "instructors", users.countByRole(Role.INSTRUCTOR),
                "totalCourses", courses.count(),
                "totalEnrollments", enrollments.count(),
                "users", users.findAll().stream().map(u -> Map.of(
                        "id", u.getId(),
                        "name", u.getName(),
                        "email", u.getEmail(),
                        "role", u.getRole().name()
                )).toList()
        );
    }

    @PatchMapping("/users/{id}/role")
    public User updateRole(@PathVariable long id, @RequestBody Map<String, String> body) {
        User user = users.findById(id).orElseThrow();
        user.setRole(Role.valueOf(body.get("role").toUpperCase()));
        return users.save(user);
    }

    @GetMapping("/users")
    public List<User> allUsers() {
        return users.findAll();
    }
}
