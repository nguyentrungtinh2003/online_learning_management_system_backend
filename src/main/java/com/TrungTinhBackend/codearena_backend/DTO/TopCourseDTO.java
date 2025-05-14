package com.TrungTinhBackend.codearena_backend.DTO;

import com.TrungTinhBackend.codearena_backend.Entity.Course;
import lombok.Data;

@Data
public class TopCourseDTO {
    private Course course;
    private Long enrollmentCount;

    public TopCourseDTO(Long aLong, Long aLong1) {
    }
}
