package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson,Long>, JpaSpecificationExecutor<Lesson> {
    List<Lesson> findByIsDeletedFalse();
    Page<Lesson> findByIsDeletedFalse(Pageable pageable);
    Page<Lesson> findByCourseIdAndIsDeletedFalse(Long courseId, Pageable pageable);
    List<Lesson> findByCourseId(Long courseId);
}
