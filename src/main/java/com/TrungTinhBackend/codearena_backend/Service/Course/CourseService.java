package com.TrungTinhBackend.codearena_backend.Service.Course;

import com.TrungTinhBackend.codearena_backend.DTO.CourseDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CourseService {
    public APIResponse addCourse(CourseDTO courseDTO, MultipartFile img) throws Exception;
    public APIResponse buyCourse(Long userId,Long courseId);
    public APIResponse getAllCourse();
    public APIResponse getCourseById(Long id);
    public APIResponse updateCourse(Long id, CourseDTO courseDTO, MultipartFile img) throws Exception;
    public APIResponse deleteCourse(Long id) throws Exception;
    public APIResponse searchCourse(String keyword, int page, int size);
    public APIResponse getCourseByUserId(Long userId);
    public APIResponse getCourseByPage(int page, int size);
}
