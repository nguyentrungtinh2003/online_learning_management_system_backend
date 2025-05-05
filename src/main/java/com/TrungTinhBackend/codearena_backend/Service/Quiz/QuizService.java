package com.TrungTinhBackend.codearena_backend.Service.Quiz;

import com.TrungTinhBackend.codearena_backend.DTO.AnswerUserDTO;
import com.TrungTinhBackend.codearena_backend.DTO.QuizDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuizService {
    public APIResponse addQuiz(QuizDTO quizDTO, MultipartFile img) throws Exception;
    public APIResponse updateQuiz(Long id, QuizDTO quizDTO, MultipartFile img) throws Exception;
    public APIResponse deleteQuiz(Long id) throws Exception;
    public APIResponse restoreQuiz(Long id);
    public APIResponse searchQuiz(String keyword, int page, int size);
    public APIResponse getQuizByPage(int page, int size);
    public APIResponse getAllQuiz();
    public APIResponse getQuizById(Long id);
    public APIResponse submitQuiz(Long id,Long userId, AnswerUserDTO answerUserDTO);
    public APIResponse getQuizByLessonIdAndByPage(Long lessonId,int page, int size);
    public APIResponse getAllQuizzessByLessonId(Long lessonId);
}
