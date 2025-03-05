package com.TrungTinhBackend.codearena_backend.Service.Question;

import com.TrungTinhBackend.codearena_backend.Entity.Lesson;
import com.TrungTinhBackend.codearena_backend.Entity.Question;
import com.TrungTinhBackend.codearena_backend.Entity.Quiz;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.QuestionRepository;
import com.TrungTinhBackend.codearena_backend.Repository.QuizRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestQuestion;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

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

            Quiz quiz = quizRepository.findById(apiRequestQuestion.getQuiz().getId()).orElseThrow(
                    () -> new NotFoundException("Quiz not found by id " + apiRequestQuestion.getQuiz().getId())
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
    }

    @Override
    public APIResponse updateQuestion(Long id, APIRequestQuestion apiRequestQuestion, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();

            Quiz quiz = quizRepository.findById(apiRequestQuestion.getQuiz().getId()).orElseThrow(
                    () -> new NotFoundException("Quiz not found by id " + apiRequestQuestion.getQuiz().getId())
            );

            Question question = questionRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Question not found by id " + id)
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
    }

    @Override
    public APIResponse deleteQuestion(Long id) throws Exception {
        APIResponse apiResponse = new APIResponse();

            Question question = questionRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Question not found by id " + id)
            );

            question.setDeleted(true);
            questionRepository.save(question);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Delete question success !");
            apiResponse.setData(question);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
    }

    @Override
    public APIResponse searchQuestion(String keyword, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<Question> questions = questionRepository.searchQuestion(keyword,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search question success !");
        apiResponse.setData(questions);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getQuestionByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<Question> questions = questionRepository.findAll(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get question by page success !");
        apiResponse.setData(questions);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getAllQuestion() {
        APIResponse apiResponse = new APIResponse();

        List<Question> questions = questionRepository.findAll();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get all question success !");
        apiResponse.setData(questions);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getQuestionById(Long id) {
        APIResponse apiResponse = new APIResponse();

        Question question = questionRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Question not found !")
        );

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get question by id success !");
        apiResponse.setData(question);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}
