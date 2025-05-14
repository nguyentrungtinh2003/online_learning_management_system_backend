package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.DTO.TopCourseDTO;
import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.Enrollment;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    boolean existsByUserAndCourse(User user, Course course);
    void deleteByUserAndCourse(User user, Course course);
    List<Enrollment> findByUserId(Long userId);

    @Query("SELECT e.course.name, COUNT(e.id) " +
            "FROM Enrollment e " +
            "GROUP BY e.course.name " +
            "ORDER BY COUNT(e.id) DESC")
    Page<Object[]> findTopCoursesByEnrollment(Pageable pageable);
}
