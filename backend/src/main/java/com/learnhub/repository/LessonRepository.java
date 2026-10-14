package com.learnhub.repository;

import com.learnhub.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByModuleIdOrderByLessonOrderAsc(Long moduleId);

    @Query("select l from Lesson l where l.module.course.id = :courseId order by l.module.moduleOrder asc, l.lessonOrder asc")
    List<Lesson> findByModuleCourseIdOrderByModuleModuleOrderAscLessonOrderAsc(@Param("courseId") Long courseId);
}
