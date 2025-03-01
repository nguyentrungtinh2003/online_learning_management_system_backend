package com.TrungTinhBackend.codearena_backend.Service.LessonComment;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestLessonComment;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface LessonCommentService {
    public APIResponse addLessonComment(APIRequestLessonComment apiRequestLessonComment, MultipartFile img, MultipartFile video) throws IOException;
    public APIResponse getAllLessonComment();
    public APIResponse getLessonCommentById(Long id);
}
