package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {

    @Query("SELECT c FROM Course c WHERE " +
    "LOWER(c.courseName) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%',:keyword,'%'))")
    Page<Course> searchCourse(@Param("keyword") String keyword, Pageable pageable);
}
