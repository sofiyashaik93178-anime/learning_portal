package com.learnhub.controller;

import com.learnhub.entity.*;
import com.learnhub.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/instructor")
public class InstructorController {
    private final CourseRepository courses;
    private final CourseModuleRepository modules;
    private final LessonRepository lessons;
    private final QuizRepository quizzes;
    private final QuestionRepository questions;
    private final EnrollmentRepository enrollments;

    public InstructorController(CourseRepository courses, CourseModuleRepository modules, LessonRepository lessons,
                                QuizRepository quizzes, QuestionRepository questions, EnrollmentRepository enrollments) {
        this.courses = courses;
        this.modules = modules;
        this.lessons = lessons;
        this.quizzes = quizzes;
        this.questions = questions;
        this.enrollments = enrollments;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard(@AuthenticationPrincipal User user) {
        List<Course> mine = user.getRole() == Role.ADMIN ? courses.findAll() : courses.findByInstructorId(user.getId());
        List<Map<String, Object>> list = mine.stream().map(c -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", c.getId());
            m.put("title", c.getTitle());
            m.put("category", c.getCategory());
            m.put("enrollments", enrollments.findByCourseId(c.getId()).size());
            return m;
        }).toList();
        return Map.of("courses", list);
    }

    @PostMapping("/courses")
    public Course createCourse(@AuthenticationPrincipal User user, @RequestBody Course body) {
        body.setId(null);
        body.setInstructor(user);
        if (body.getPassScore() == null) body.setPassScore(70.0);
        return courses.save(body);
    }

    @PostMapping("/courses/{id}/modules")
    public CourseModule addModule(@PathVariable long id, @RequestBody CourseModule body) {
        Course course = courses.findById(id).orElseThrow();
        body.setId(null);
        body.setCourse(course);
        if (body.getModuleOrder() == null) body.setModuleOrder(modules.findByCourseIdOrderByModuleOrderAsc(id).size() + 1);
        return modules.save(body);
    }

    @PostMapping("/modules/{id}/lessons")
    public Lesson addLesson(@PathVariable long id, @RequestBody Lesson body) {
        CourseModule module = modules.findById(id).orElseThrow();
        body.setId(null);
        body.setModule(module);
        if (body.getLessonOrder() == null) body.setLessonOrder(lessons.findByModuleIdOrderByLessonOrderAsc(id).size() + 1);
        return lessons.save(body);
    }

    @PostMapping("/lessons/{id}/quiz")
    public ResponseEntity<?> addQuiz(@PathVariable long id, @RequestBody Map<String, Object> body) {
        Lesson lesson = lessons.findById(id).orElseThrow();
        Quiz quiz = Quiz.builder()
                .title(String.valueOf(body.getOrDefault("title", "Lesson Quiz")))
                .lesson(lesson)
                .course(lesson.getModule().getCourse())
                .build();
        quizzes.save(quiz);
        return ResponseEntity.ok(quiz);
    }

    @PostMapping("/quizzes/{id}/questions")
    public Question addQuestion(@PathVariable long id, @RequestBody Question body) {
        Quiz quiz = quizzes.findById(id).orElseThrow();
        body.setId(null);
        body.setQuiz(quiz);
        return questions.save(body);
    }
}
