package com.TrungTinhBackend.codearena_backend.Service.Question;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestLesson;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestQuestion;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionService {
    public APIResponse addQuestion(APIRequestQuestion apiRequestQuestion, MultipartFile img) throws Exception;
    public APIResponse updateQuestion(Long id, APIRequestQuestion apiRequestQuestion, MultipartFile img) throws Exception;
    public APIResponse deleteQuestion(Long id) throws Exception;
}
