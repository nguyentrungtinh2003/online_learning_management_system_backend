package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.Blog;
import com.TrungTinhBackend.codearena_backend.Entity.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {
    @Query("SELECT q FROM Quiz q WHERE " +
            "LOWER(q.quizName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(q.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Quiz> searchQuiz(@Param("keyword") String keyword, Pageable pageable);
}
