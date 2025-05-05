package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.Blog;
import com.TrungTinhBackend.codearena_backend.Entity.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long>, JpaSpecificationExecutor<Quiz> {
    Page<Quiz> findByLessonIdAndIsDeletedFalse(Long lessonId, Pageable pageable);
    List<Quiz> findByLessonId(Long lessonId);
}
