package com.TrungTinhBackend.codearena_backend.Service.Lesson;

import com.TrungTinhBackend.codearena_backend.DTO.LessonDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

public interface LessonService {
    public APIResponse addLesson(LessonDTO lessonDTO, MultipartFile img, MultipartFile video) throws Exception;
    public APIResponse getAllLesson();
    public APIResponse getLessonById(Long id);
    public APIResponse updateLesson(Long id, LessonDTO lessonDTO, MultipartFile img, MultipartFile video) throws Exception;
    public APIResponse deleteLesson(Long id) throws Exception;
    public APIResponse searchLesson(String keyword, int page, int size);
    public APIResponse getLessonByPage(int page, int size);
    public APIResponse getLessonByCourseIdAndByPage(Long courseId,int page, int size);
    public APIResponse restoreLesson(Long id);
}
