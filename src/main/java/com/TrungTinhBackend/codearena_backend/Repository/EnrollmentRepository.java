package com.TrungTinhBackend.codearena_backend.Repository;

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

    @Query("SELECT e.courseId, COUNT(e.id) as enrollCount " +
            "FROM Enrollment e " +
            "GROUP BY e.courseId " +
            "ORDER BY enrollCount DESC")
    Page<Object[]> findTopCoursesByEnrollment(Pageable pageable);  // Đổi List<Object[]> thành Page<Object[]>


}
