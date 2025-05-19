package com.TrungTinhBackend.codearena_backend.Service.Question;

import com.TrungTinhBackend.codearena_backend.Entity.Question;
import com.TrungTinhBackend.codearena_backend.Entity.Quiz;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.QuestionRepository;
import com.TrungTinhBackend.codearena_backend.Repository.QuizRepository;
import com.TrungTinhBackend.codearena_backend.DTO.QuestionDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.QuestionSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    public QuestionServiceImpl(QuestionRepository questionRepository, ImgService imgService, QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.imgService = imgService;
        this.quizRepository = quizRepository;
    }

    @Override
    public APIResponse addQuestion(QuestionDTO questionDTO, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();

            Quiz quiz = quizRepository.findById(questionDTO.getQuizId()).orElseThrow(
                    () -> new NotFoundException("Quiz not found by id " + questionDTO.getQuizId())
            );

            Question question = new Question();

            question.setQuestionName(questionDTO.getQuestionName());
            question.setAnswerA(questionDTO.getAnswerA());
            question.setAnswerB(questionDTO.getAnswerB());
            question.setAnswerC(questionDTO.getAnswerC());
            question.setAnswerD(questionDTO.getAnswerD());
            question.setAnswerCorrect(questionDTO.getAnswerCorrect());
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
    public APIResponse updateQuestion(Long id, QuestionDTO questionDTO, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();

//            Quiz quiz = quizRepository.findById(questionDTO.getQuizId()).orElseThrow(
//                    () -> new NotFoundException("Quiz not found by id " + questionDTO.getQuizId())
//            );

            Question question = questionRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Question not found by id " + id)
            );

            if(questionDTO.getQuestionName() != null && !questionDTO.getQuestionName().isEmpty()) {
                question.setQuestionName(questionDTO.getQuestionName());
            }
            if(questionDTO.getAnswerA() != null && !questionDTO.getAnswerA().isEmpty()) {
                question.setAnswerA(questionDTO.getAnswerA());
            }
            if(questionDTO.getAnswerB() != null && !questionDTO.getAnswerB().isEmpty()) {
                question.setAnswerB(questionDTO.getAnswerB());
            }
            if(questionDTO.getAnswerC() != null && !questionDTO.getAnswerC().isEmpty()) {
                question.setAnswerC(questionDTO.getAnswerC());
            }
            if(questionDTO.getAnswerD() != null && !questionDTO.getAnswerD().isEmpty()) {
                question.setAnswerD(questionDTO.getAnswerD());
            }
            if(questionDTO.getAnswerCorrect() != null && !questionDTO.getAnswerCorrect().isEmpty()) {
                question.setAnswerCorrect(questionDTO.getAnswerCorrect());
            }
            if(img != null) {
                question.setImg(imgService.updateImg(question.getImg(),img));
            }
//            if(quiz != null) {
//                question.setQuiz(quiz);
//            }

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
    public APIResponse restoreQuestion(Long id) {
        APIResponse apiResponse = new APIResponse();

        Question question = questionRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Question not found by id " + id)
        );

        question.setDeleted(false);
        questionRepository.save(question);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Restore question success !");
        apiResponse.setData(question);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse searchQuestion(String keyword, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Specification<Question> specification = QuestionSpecification.searchByKeyword(keyword);
        Page<Question> questions = questionRepository.findAll(specification,pageable);

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

    @Override
    public APIResponse getQuestionByQuizIdAndByPage(Long quizId, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<Question> questions = questionRepository.findByQuizIdAndIsDeletedFalse(quizId,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get question by quiz id success !");
        apiResponse.setData(questions);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}
