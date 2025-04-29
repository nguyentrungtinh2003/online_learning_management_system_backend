package com.TrungTinhBackend.codearena_backend.Service.Question;

import com.TrungTinhBackend.codearena_backend.DTO.QuestionDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionService {
    public APIResponse addQuestion(QuestionDTO questionDTO, MultipartFile img) throws Exception;
    public APIResponse updateQuestion(Long id, QuestionDTO questionDTO, MultipartFile img) throws Exception;
    public APIResponse deleteQuestion(Long id) throws Exception;
    public APIResponse searchQuestion(String keyword, int page, int size);
    public APIResponse getQuestionByPage(int page, int size);
    public APIResponse getAllQuestion();
    public APIResponse getQuestionById(Long id);
    public APIResponse getQuestionByQuizIdAndByPage(Long quizId, int page, int size);
}
