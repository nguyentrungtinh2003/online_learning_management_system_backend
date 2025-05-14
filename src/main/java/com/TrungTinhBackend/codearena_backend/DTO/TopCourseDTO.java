package com.TrungTinhBackend.codearena_backend.DTO;

import lombok.Data;

@Data
public class TopCourseDTO {
    private String courseName;
    private Long enrollmentCount;

    public TopCourseDTO(Long aLong, Long aLong1) {
    }
}
