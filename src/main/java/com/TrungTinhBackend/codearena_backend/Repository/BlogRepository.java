package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog> {
    List<Blog> findByUserId(Long userId);

    @Query("SELECT b FROM Blog b JOIN b.likedUsers u WHERE u.id = :userId")
    List<Blog> findBlogsLikedByUser(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM Blog ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Blog> findRandomBlogs(@Param("limit") int limit);
}
