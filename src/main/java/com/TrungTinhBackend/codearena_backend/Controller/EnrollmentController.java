package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Enrollment.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/enroll/{userId}/{courseId}")
    public ResponseEntity<APIResponse> enrollUser(@PathVariable Long userId, @PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.enrollUser(userId, courseId));
    }

    @GetMapping("/enroll/{userId}")
    public ResponseEntity<APIResponse> getEnrollByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(enrollmentService.getEnrollByUserId(userId));
    }

    @GetMapping("/enroll/top-enrollments")
    public ResponseEntity<APIResponse> getTopEnrolledCourses() {
        return ResponseEntity.ok(enrollmentService.getTopEnrolledCourses());
    }

}
