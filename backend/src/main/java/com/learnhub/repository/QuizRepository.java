package com.learnhub.repository;

import com.learnhub.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByLessonId(Long lessonId);
    List<Quiz> findByCourseId(Long courseId);
}
