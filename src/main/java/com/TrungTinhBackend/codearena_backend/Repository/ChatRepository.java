package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.Blog;
import com.TrungTinhBackend.codearena_backend.Entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Long> {
    @Query("SELECT c FROM Chat c WHERE " +
            "LOWER(c.message) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Chat> searchChat(@Param("keyword") String keyword, Pageable pageable);
}
