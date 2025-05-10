package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessRepository extends JpaRepository<Process,Long> {
    // Tìm tiến độ theo user và bài học
    Process findByUserIdAndLessonId(Long userId, Long lessonId);

    // Đếm tổng số bài học đã có tiến độ (lesson != null)
    Long countByCourseIdAndUserIdAndLessonIsNotNull(Long courseId, Long userId);

    // Đếm số bài học đã hoàn thành (completionPercent = 100)
    Long countByCourseIdAndUserIdAndLessonIsNotNullAndCompletionPercent(Long courseId, Long userId, Integer completionPercent);

    // Tìm process của khóa học tổng quan (lesson = null)
    Process findByUserIdAndCourseIdAndLessonIsNull(Long userId, Long courseId);
}
