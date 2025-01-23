package com.TrungTinhBackend.codearena_backend.Service.Question;

import com.TrungTinhBackend.codearena_backend.Entity.Lesson;
import com.TrungTinhBackend.codearena_backend.Entity.Question;
import com.TrungTinhBackend.codearena_backend.Entity.Quiz;
import com.TrungTinhBackend.codearena_backend.Repository.QuestionRepository;
import com.TrungTinhBackend.codearena_backend.Repository.QuizRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestQuestion;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class QuestionServiceImpl implements QuestionService{

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ImgService imgService;

    @Autowired
    private QuizRepository quizRepository;

    @Override
    public APIResponse addQuestion(APIRequestQuestion apiRequestQuestion, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {
            Quiz quiz = quizRepository.findById(apiRequestQuestion.getQuiz().getId()).orElseThrow(
                    () -> new RuntimeException("Quiz not found !")
            );

            Question question = new Question();

            question.setQuestionName(apiRequestQuestion.getQuestionName());
            question.setAnswerA(apiRequestQuestion.getAnswerA());
            question.setAnswerB(apiRequestQuestion.getAnswerB());
            question.setAnswerC(apiRequestQuestion.getAnswerC());
            question.setAnswerD(apiRequestQuestion.getAnswerD());
            question.setAnswerCorrect(apiRequestQuestion.getAnswerCorrect());
            question.setDeleted(false);
            if(img != null) {
                question.setImg(imgService.uploadImg(img));
            }
            question.setQuiz(quiz);

            questionRepository.save(question);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Add question success !");
            apiResponse.setData(question);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }

    @Override
    public APIResponse updateQuestion(Long id, APIRequestQuestion apiRequestQuestion, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {
            Quiz quiz = quizRepository.findById(apiRequestQuestion.getQuiz().getId()).orElseThrow(
                    () -> new RuntimeException("Quiz not found !")
            );

            Question question = questionRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("Question not found !")
            );

            if(apiRequestQuestion.getQuestionName() != null && !apiRequestQuestion.getQuestionName().isEmpty()) {
                question.setQuestionName(apiRequestQuestion.getQuestionName());
            }
            if(apiRequestQuestion.getAnswerA() != null && !apiRequestQuestion.getAnswerA().isEmpty()) {
                question.setAnswerA(apiRequestQuestion.getAnswerA());
            }
            if(apiRequestQuestion.getAnswerB() != null && !apiRequestQuestion.getAnswerB().isEmpty()) {
                question.setAnswerB(apiRequestQuestion.getAnswerB());
            }
            if(apiRequestQuestion.getAnswerC() != null && !apiRequestQuestion.getAnswerC().isEmpty()) {
                question.setAnswerC(apiRequestQuestion.getAnswerC());
            }
            if(apiRequestQuestion.getAnswerD() != null && !apiRequestQuestion.getAnswerD().isEmpty()) {
                question.setAnswerD(apiRequestQuestion.getAnswerD());
            }
            if(apiRequestQuestion.getAnswerCorrect() != null && !apiRequestQuestion.getAnswerCorrect().isEmpty()) {
                question.setAnswerCorrect(apiRequestQuestion.getAnswerCorrect());
            }
            if(img != null) {
                question.setImg(imgService.updateImg(question.getImg(),img));
            }
            if(quiz != null) {
                question.setQuiz(quiz);
            }

            questionRepository.save(question);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Update question success !");
            apiResponse.setData(question);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }

    @Override
    public APIResponse deleteQuestion(Long id) throws Exception {
        return null;
    }
}
