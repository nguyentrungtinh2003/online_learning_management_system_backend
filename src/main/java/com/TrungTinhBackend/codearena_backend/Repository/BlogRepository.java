package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Long> {
    @Query("SELECT b FROM Blog b WHERE " +
            "LOWER(b.blogName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Blog> searchBlog(@Param("keyword") String keyword, Pageable pageable);
}
