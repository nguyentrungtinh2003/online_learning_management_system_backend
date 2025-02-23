package com.TrungTinhBackend.codearena_backend.Repository;
import com.TrungTinhBackend.codearena_backend.Entity.LessonComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonCommentRepository extends JpaRepository<LessonComment,Long> {
}
