package com.TrungTinhBackend.codearena_backend.Service.LessonComment;

import com.TrungTinhBackend.codearena_backend.DTO.LessonCommentDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface LessonCommentService {
    public APIResponse addLessonComment(LessonCommentDTO lessonCommentDTO) throws IOException;
    public APIResponse getAllLessonComment();
    public APIResponse getLessonCommentById(Long id);
    public APIResponse getLessonCommentByLessonId(Long lessonId);
    public APIResponse deleteLessonComment(Long id, Long userId);
    public APIResponse searchLessonComment(String keyword, int page, int size);
    public APIResponse getLessonCommentByPage(int page, int size);
}
