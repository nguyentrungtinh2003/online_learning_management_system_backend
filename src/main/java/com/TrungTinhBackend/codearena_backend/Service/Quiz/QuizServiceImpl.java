package com.TrungTinhBackend.codearena_backend.Service.Quiz;

import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.Lesson;
import com.TrungTinhBackend.codearena_backend.Entity.Quiz;
import com.TrungTinhBackend.codearena_backend.Repository.LessonRepository;
import com.TrungTinhBackend.codearena_backend.Repository.QuizRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestQuiz;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class QuizServiceImpl implements QuizService{

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private ImgService imgService;

    @Autowired
    private LessonRepository lessonRepository;


    @Override
    public APIResponse addQuiz(APIRequestQuiz apiRequestQuiz, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {
            Lesson lesson = lessonRepository.findById(apiRequestQuiz.getLesson().getId()).orElseThrow(
                    () -> new RuntimeException("Lesson not found !")
            );

            Quiz quiz = new Quiz();

            quiz.setQuizName(apiRequestQuiz.getQuizName());
            quiz.setDescription(apiRequestQuiz.getDescription());
            quiz.setDeleted(false);
            quiz.setDate(LocalDateTime.now());

            if(img != null) {
                quiz.setImg(imgService.uploadImg(img));
            }
            quiz.setLesson(lesson);
            quiz.setQuestions(null);

            quizRepository.save(quiz);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Add quiz success !");
            apiResponse.setData(quiz);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }

    @Override
    public APIResponse updateQuiz(Long id, APIRequestQuiz apiRequestQuiz, MultipartFile img) throws Exception {
        return null;
    }

    @Override
    public APIResponse deleteQuiz(Long id) throws Exception {
        return null;
    }
}
