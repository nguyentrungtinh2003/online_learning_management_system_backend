package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.Question;
import com.TrungTinhBackend.codearena_backend.Entity.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
    @Query("SELECT q FROM Question q WHERE " +
            "LOWER(q.questionName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(q.answerA) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(q.answerB) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(q.answerC) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(q.answerD) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(q.answerCorrect) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Question> searchQuestion(@Param("keyword") String keyword, Pageable pageable);
}
