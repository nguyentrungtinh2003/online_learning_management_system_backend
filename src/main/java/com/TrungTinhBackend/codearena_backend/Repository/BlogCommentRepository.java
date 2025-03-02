package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.BlogComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogCommentRepository extends JpaRepository<BlogComment,Long> {
    @Query("SELECT b FROM BlogComment b WHERE " +
            "LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<BlogComment> searchBlogComment(@Param("keyword") String keyword, Pageable pageable);
}
