package com.TrungTinhBackend.codearena_backend.Repository;
import com.TrungTinhBackend.codearena_backend.Entity.LessonComment;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonCommentRepository extends JpaRepository<LessonComment,Long> {
    @Query("SELECT l FROM LessonComment l WHERE " +
            "LOWER(l.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<LessonComment> searchLessonComment(@Param("keyword") String keyword, Pageable pageable);
}
