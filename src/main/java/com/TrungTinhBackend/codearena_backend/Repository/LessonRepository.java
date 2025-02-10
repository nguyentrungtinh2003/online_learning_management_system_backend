package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson,Long> {

    @Query("SELECT l FROM Lesson l WHERE " +
            "LOWER(l.lessonName) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
            "LOWER(l.description) LIKE LOWER(CONCAT('%',:keyword,'%'))")
    Page<Lesson> searchLesson(@Param("keyword") String keyword, Pageable pageable);
}
