package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessRepository extends JpaRepository<Process,Long> {
    Process findByUserIdAndLessonId(Long userId, Long lessonId);
    Long countByCourseIdAndLessonIsNotNull(Long courseId);
    Long countByCourseIdAndUserIdAndCompletionPercent(Long courseId, Long userId, Integer value);

    Process findByUserIdAndCourseIdAndLessonIsNull(Long userId, Long courseId);
}
