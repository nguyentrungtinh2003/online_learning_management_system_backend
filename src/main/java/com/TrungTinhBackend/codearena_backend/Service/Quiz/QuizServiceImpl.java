package com.TrungTinhBackend.codearena_backend.Service.Quiz;

import com.TrungTinhBackend.codearena_backend.Entity.Lesson;
import com.TrungTinhBackend.codearena_backend.Entity.Question;
import com.TrungTinhBackend.codearena_backend.Entity.Quiz;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.LessonRepository;
import com.TrungTinhBackend.codearena_backend.Repository.QuestionRepository;
import com.TrungTinhBackend.codearena_backend.Repository.QuizRepository;
import com.TrungTinhBackend.codearena_backend.DTO.QuizDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.QuizSpecification;
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
public class QuizServiceImpl implements QuizService{

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private ImgService imgService;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public QuizServiceImpl(QuizRepository quizRepository, ImgService imgService, LessonRepository lessonRepository, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.imgService = imgService;
        this.lessonRepository = lessonRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public APIResponse addQuiz(QuizDTO quizDTO, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();

            Lesson lesson = lessonRepository.findById(quizDTO.getLesson().getId()).orElseThrow(
                    () -> new NotFoundException("Lesson not found by id " + quizDTO.getLesson().getId())
            );

            Quiz quiz = new Quiz();

            quiz.setQuizName(quizDTO.getQuizName());
            quiz.setDescription(quizDTO.getDescription());
            quiz.setPrice(quizDTO.getPrice());
            quiz.setQuizEnum(quizDTO.getQuizEnum());
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
    }

    @Override
    public APIResponse updateQuiz(Long id, QuizDTO quizDTO, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();

            Lesson lesson = lessonRepository.findById(quizDTO.getLesson().getId()).orElseThrow(
                    () -> new NotFoundException("Lesson not found by id " + quizDTO.getLesson().getId())
            );

            Quiz quiz = quizRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Quiz not found by id " + id)
            );
            if(quizDTO.getQuizName() != null && !quizDTO.getQuizName().isEmpty()) {
                quiz.setQuizName(quizDTO.getQuizName());
            }
            if(quizDTO.getDescription() != null && !quizDTO.getDescription().isEmpty()) {
                quiz.setDescription(quizDTO.getDescription());
            }
            if(quizDTO.getPrice() != null && !quizDTO.getPrice().isInfinite()) {
                quiz.setPrice(quizDTO.getPrice());
            }
            if(quizDTO.getQuizEnum() != null ) {
                quiz.setQuizEnum(quizDTO.getQuizEnum());
            }
            if(img != null) {
                quiz.setImg(imgService.updateImg(quiz.getImg(),img));
            }
            if(lesson != null) {
                quiz.setLesson(lesson);
            }
            if (quizDTO.getQuestions() != null && !quizDTO.getQuestions().isEmpty()) {
                List<Question> questionList = questionRepository.findAllById(
                        quizDTO.getQuestions().stream().map(Question::getId).toList()
                );

                if (questionList.size() != quizDTO.getQuestions().size()) {
                    throw new NotFoundException("One or more questions not found !");
                }

                quiz.setQuestions(questionList);
            }

            quizRepository.save(quiz);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Update quiz success !");
            apiResponse.setData(quiz);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
    }

    @Override
    public APIResponse deleteQuiz(Long id) throws Exception {
        APIResponse apiResponse = new APIResponse();

            Quiz quiz = quizRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Quiz not found by id " + id)
            );

            quiz.setDeleted(true);
            quizRepository.save(quiz);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Delete quiz success !");
            apiResponse.setData(quiz);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
    }

    @Override
    public APIResponse searchQuiz(String keyword, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Specification<Quiz> specification = QuizSpecification.searchByKeyword(keyword);
        Page<Quiz> quizzes = quizRepository.findAll(specification,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search quiz success !");
        apiResponse.setData(quizzes);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getQuizByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Quiz> quizzes = quizRepository.findAll(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get quiz by page success !");
        apiResponse.setData(quizzes);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getAllQuiz() {
        APIResponse apiResponse = new APIResponse();

        List<Quiz> quizzes = quizRepository.findAll();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get all quiz success !");
        apiResponse.setData(quizzes);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getQuizById(Long id) {
        APIResponse apiResponse = new APIResponse();

        Quiz quiz = quizRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Quiz not found !")
        );

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get quiz by id success !");
        apiResponse.setData(quiz);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}
