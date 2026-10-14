package com.learnhub.repository;

import com.learnhub.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    Optional<Progress> findByStudentIdAndLessonId(Long studentId, Long lessonId);
    List<Progress> findByStudentId(Long studentId);

    @Query("select count(p) from Progress p where p.student.id = :studentId and p.lesson.module.course.id = :courseId and p.status = :status")
    long countByStudentIdAndLessonModuleCourseIdAndStatus(@Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("status") String status);
}
