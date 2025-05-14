package com.TrungTinhBackend.codearena_backend.Service.Enrollment;

import com.TrungTinhBackend.codearena_backend.Response.APIResponse;

public interface EnrollmentService {
    public APIResponse enrollUser(Long userId, Long courseId);
    public APIResponse getEnrollByUserId(Long userId);
    public APIResponse getTopEnrolledCourses();
}
