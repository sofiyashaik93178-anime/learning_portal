package com.learnhub.service;

import com.learnhub.entity.*;
import com.learnhub.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

@Service
public class LearningService {
    private final CourseRepository courses;
    private final CourseModuleRepository modules;
    private final LessonRepository lessons;
    private final EnrollmentRepository enrollments;
    private final ProgressRepository progressRepo;
    private final QuizRepository quizzes;
    private final QuestionRepository questions;
    private final QuizAttemptRepository attempts;
    private final CertificateRepository certificates;

    public LearningService(CourseRepository courses, CourseModuleRepository modules, LessonRepository lessons,
                           EnrollmentRepository enrollments, ProgressRepository progressRepo, QuizRepository quizzes,
                           QuestionRepository questions, QuizAttemptRepository attempts, CertificateRepository certificates) {
        this.courses = courses;
        this.modules = modules;
        this.lessons = lessons;
        this.enrollments = enrollments;
        this.progressRepo = progressRepo;
        this.quizzes = quizzes;
        this.questions = questions;
        this.attempts = attempts;
        this.certificates = certificates;
    }

    public List<Map<String, Object>> listCourses() {
        return courses.findAll().stream().map(this::courseCard).toList();
    }

    public Map<String, Object> courseDetail(long id, User student) {
        Course course = courses.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<CourseModule> mods = modules.findByCourseIdOrderByModuleOrderAsc(id);
        List<Lesson> allLessons = lessons.findByModuleCourseIdOrderByModuleModuleOrderAscLessonOrderAsc(id);
        boolean enrolled = student != null && enrollments.existsByStudentIdAndCourseId(student.getId(), id);
        List<Map<String, Object>> moduleViews = new ArrayList<>();
        int idx = 0;
        Set<Long> completed = new HashSet<>();
        if (student != null) {
            progressRepo.findByStudentId(student.getId()).stream()
                    .filter(p -> "COMPLETED".equals(p.getStatus()))
                    .forEach(p -> completed.add(p.getLesson().getId()));
        }
        for (CourseModule m : mods) {
            List<Map<String, Object>> lessonViews = new ArrayList<>();
            for (Lesson l : allLessons.stream().filter(x -> x.getModule().getId().equals(m.getId())).toList()) {
                boolean unlocked = idx == 0 || completed.contains(allLessons.get(idx - 1).getId());
                if (!enrolled) unlocked = idx == 0;
                Map<String, Object> lv = new LinkedHashMap<>();
                lv.put("id", l.getId());
                lv.put("title", l.getTitle());
                lv.put("durationMinutes", l.getDurationMinutes());
                lv.put("completed", completed.contains(l.getId()));
                lv.put("unlocked", enrolled && unlocked);
                lv.put("hasQuiz", quizzes.findByLessonId(l.getId()).isPresent());
                lessonViews.add(lv);
                idx++;
            }
            moduleViews.add(Map.of("id", m.getId(), "title", m.getTitle(), "order", m.getModuleOrder(), "lessons", lessonViews));
        }
        Map<String, Object> view = new LinkedHashMap<>(courseCard(course));
        view.put("enrolled", enrolled);
        view.put("modules", moduleViews);
        view.put("progressPercent", student == null ? 0 : progressPercent(student.getId(), id, allLessons.size()));
        view.put("certificate", student == null ? null : certificates.findByStudentIdAndCourseId(student.getId(), id)
                .map(this::certView).orElse(null));
        return view;
    }

    public Map<String, Object> enroll(User student, long courseId) {
        Course course = courses.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        enrollments.findByStudentIdAndCourseId(student.getId(), courseId).ifPresent(e -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already enrolled");
        });
        Enrollment e = Enrollment.builder().student(student).course(course).status("ACTIVE").build();
        enrollments.save(e);
        return Map.of("message", "Enrolled", "courseId", courseId);
    }

    public Map<String, Object> lessonForStudent(User student, long lessonId) {
        Lesson lesson = lessons.findById(lessonId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Long courseId = lesson.getModule().getCourse().getId();
        if (!enrollments.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Enroll to access this lesson");
        }
        if (!isUnlocked(student.getId(), lesson)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Complete the previous lesson first");
        }
        Map<String, Object> view = new LinkedHashMap<>();
        view.put("id", lesson.getId());
        view.put("title", lesson.getTitle());
        view.put("content", lesson.getContent());
        view.put("videoUrl", lesson.getVideoUrl());
        view.put("materialUrl", lesson.getMaterialUrl());
        view.put("durationMinutes", lesson.getDurationMinutes());
        view.put("courseId", courseId);
        view.put("courseTitle", lesson.getModule().getCourse().getTitle());
        view.put("moduleTitle", lesson.getModule().getTitle());
        quizzes.findByLessonId(lessonId).ifPresent(q -> {
            view.put("quiz", quizView(q, false));
        });
        return view;
    }

    public Map<String, Object> completeLesson(User student, long lessonId) {
        Lesson lesson = lessons.findById(lessonId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (quizzes.findByLessonId(lessonId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pass the quiz to complete this lesson");
        }
        markComplete(student, lesson);
        maybeIssueCertificate(student, lesson.getModule().getCourse());
        return Map.of("status", "COMPLETED");
    }

    public Map<String, Object> submitQuiz(User student, long quizId, Map<String, String> answers) {
        Quiz quiz = quizzes.findById(quizId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<Question> qs = questions.findByQuizId(quizId);
        int correct = 0;
        for (Question q : qs) {
            String ans = answers.get(String.valueOf(q.getId()));
            if (q.getCorrectOption().equalsIgnoreCase(ans)) correct++;
        }
        double score = qs.isEmpty() ? 0 : (correct * 100.0 / qs.size());
        Course course = quiz.getCourse() != null ? quiz.getCourse() : quiz.getLesson().getModule().getCourse();
        double pass = course.getPassScore() == null ? 70 : course.getPassScore();
        boolean passed = score >= pass;
        QuizAttempt attempt = QuizAttempt.builder().student(student).quiz(quiz).score(score).passed(passed).build();
        attempts.save(attempt);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("score", score);
        result.put("passed", passed);
        result.put("required", pass);
        result.put("correct", correct);
        result.put("total", qs.size());
        if (passed && quiz.getLesson() != null) {
            markComplete(student, quiz.getLesson());
            maybeIssueCertificate(student, course);
            result.put("practice", null);
        } else {
            result.put("practice", "Review the lesson notes and retry. Focus on the ideas you missed — growth takes another pass.");
        }
        return result;
    }

    public Map<String, Object> studentDashboard(User student) {
        List<Enrollment> mine = enrollments.findByStudentId(student.getId());
        List<Map<String, Object>> courseViews = new ArrayList<>();
        for (Enrollment e : mine) {
            List<Lesson> all = lessons.findByModuleCourseIdOrderByModuleModuleOrderAscLessonOrderAsc(e.getCourse().getId());
            Map<String, Object> card = new LinkedHashMap<>(courseCard(e.getCourse()));
            card.put("status", e.getStatus());
            card.put("progressPercent", progressPercent(student.getId(), e.getCourse().getId(), all.size()));
            card.put("enrolledAt", e.getEnrolledAt());
            courseViews.add(card);
        }
        return Map.of(
                "courses", courseViews,
                "quizAttempts", attempts.findByStudentId(student.getId()).stream().map(a -> Map.of(
                        "id", a.getId(),
                        "quizTitle", a.getQuiz().getTitle(),
                        "score", a.getScore(),
                        "passed", a.getPassed(),
                        "attemptedAt", a.getAttemptedAt()
                )).toList(),
                "certificates", certificates.findByStudentId(student.getId()).stream().map(this::certView).toList()
        );
    }

    public Map<String, Object> adminStats() {
        return Map.of(
                "users", Map.of(
                        "total", enrollments.getClass() == null ? 0 : 0
                )
        );
    }

    public Map<String, Object> quizView(Quiz quiz, boolean includeAnswers) {
        List<Map<String, Object>> qs = questions.findByQuizId(quiz.getId()).stream().map(q -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", q.getId());
            m.put("questionText", q.getQuestionText());
            m.put("optionA", q.getOptionA());
            m.put("optionB", q.getOptionB());
            m.put("optionC", q.getOptionC());
            m.put("optionD", q.getOptionD());
            if (includeAnswers) m.put("correctOption", q.getCorrectOption());
            return m;
        }).toList();
        return Map.of("id", quiz.getId(), "title", quiz.getTitle(), "questions", qs);
    }

    private void markComplete(User student, Lesson lesson) {
        Progress p = progressRepo.findByStudentIdAndLessonId(student.getId(), lesson.getId())
                .orElse(Progress.builder().student(student).lesson(lesson).build());
        p.setStatus("COMPLETED");
        p.setCompletedAt(Instant.now());
        progressRepo.save(p);
    }

    private void maybeIssueCertificate(User student, Course course) {
        List<Lesson> all = lessons.findByModuleCourseIdOrderByModuleModuleOrderAscLessonOrderAsc(course.getId());
        long done = progressRepo.countByStudentIdAndLessonModuleCourseIdAndStatus(student.getId(), course.getId(), "COMPLETED");
        if (all.isEmpty() || done < all.size()) return;
        if (certificates.findByStudentIdAndCourseId(student.getId(), course.getId()).isPresent()) return;
        String code = "LH-" + course.getId() + "-" + student.getId() + "-" + (System.currentTimeMillis() % 100000);
        certificates.save(Certificate.builder().student(student).course(course).certificateCode(code).build());
        enrollments.findByStudentIdAndCourseId(student.getId(), course.getId()).ifPresent(e -> {
            e.setStatus("COMPLETED");
            enrollments.save(e);
        });
    }

    private boolean isUnlocked(Long studentId, Lesson lesson) {
        List<Lesson> all = lessons.findByModuleCourseIdOrderByModuleModuleOrderAscLessonOrderAsc(lesson.getModule().getCourse().getId());
        int idx = 0;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(lesson.getId())) { idx = i; break; }
        }
        if (idx == 0) return true;
        return progressRepo.findByStudentIdAndLessonId(studentId, all.get(idx - 1).getId())
                .map(p -> "COMPLETED".equals(p.getStatus())).orElse(false);
    }

    private int progressPercent(Long studentId, Long courseId, int total) {
        if (total == 0) return 0;
        long done = progressRepo.countByStudentIdAndLessonModuleCourseIdAndStatus(studentId, courseId, "COMPLETED");
        return (int) Math.round(done * 100.0 / total);
    }

    public Map<String, Object> courseCard(Course c) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", c.getId());
        m.put("title", c.getTitle());
        m.put("description", c.getDescription());
        m.put("category", c.getCategory());
        m.put("level", c.getLevel());
        m.put("thumbnail", c.getThumbnail());
        m.put("durationHours", c.getDurationHours());
        m.put("passScore", c.getPassScore());
        m.put("instructorName", c.getInstructor() != null ? c.getInstructor().getName() : "Learn Hub");
        return m;
    }

    public Map<String, Object> certView(Certificate c) {
        return Map.of(
                "id", c.getId(),
                "certificateCode", c.getCertificateCode(),
                "courseTitle", c.getCourse().getTitle(),
                "studentName", c.getStudent().getName(),
                "issuedAt", c.getIssuedAt()
        );
    }
}
