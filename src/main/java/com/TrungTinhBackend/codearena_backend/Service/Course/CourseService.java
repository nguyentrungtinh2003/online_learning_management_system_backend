package com.TrungTinhBackend.codearena_backend.Service.Course;

import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestCourse;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CourseService {
    public APIResponse addCourse(APIRequestCourse apiRequestCourse, MultipartFile img) throws Exception;
    public APIResponse updateCourse(Long id, APIRequestCourse apiRequestCourse, MultipartFile img) throws Exception;
    public APIResponse deleteCourse(Long id) throws Exception;
    public APIResponse searchCourse(String keyword, int page, int size);
}
