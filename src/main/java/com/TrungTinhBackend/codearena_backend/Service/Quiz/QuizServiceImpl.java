package com.TrungTinhBackend.codearena_backend.Service.Quiz;

import com.TrungTinhBackend.codearena_backend.DTO.AnswerUserDTO;
import com.TrungTinhBackend.codearena_backend.DTO.NotificationDTO;
import com.TrungTinhBackend.codearena_backend.DTO.UserPointHistoryDTO;
import com.TrungTinhBackend.codearena_backend.Entity.Lesson;
import com.TrungTinhBackend.codearena_backend.Entity.Question;
import com.TrungTinhBackend.codearena_backend.Entity.Quiz;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.LessonRepository;
import com.TrungTinhBackend.codearena_backend.Repository.QuestionRepository;
import com.TrungTinhBackend.codearena_backend.Repository.QuizRepository;
import com.TrungTinhBackend.codearena_backend.DTO.QuizDTO;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Notification.NotificationService;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.QuizSpecification;
import com.TrungTinhBackend.codearena_backend.Service.User.UserService;
import com.TrungTinhBackend.codearena_backend.Service.UserPointHistory.UserPointHistoryService;
import com.TrungTinhBackend.codearena_backend.Service.WebSocket.WebSocketSender;
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
import java.util.stream.Collectors;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebSocketSender webSocketSender;

    @Autowired
    private UserService userService;

    @Autowired
    private UserPointHistoryService userPointHistoryService;

    @Autowired
    private NotificationService notificationService;

    public QuizServiceImpl(QuizRepository quizRepository, ImgService imgService, LessonRepository lessonRepository, QuestionRepository questionRepository, UserRepository userRepository, WebSocketSender webSocketSender, UserService userService, UserPointHistoryService userPointHistoryService, NotificationService notificationService) {
        this.quizRepository = quizRepository;
        this.imgService = imgService;
        this.lessonRepository = lessonRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.webSocketSender = webSocketSender;
        this.userService = userService;
        this.userPointHistoryService = userPointHistoryService;
        this.notificationService = notificationService;
    }

    @Override
    public APIResponse addQuiz(QuizDTO quizDTO, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();

            Lesson lesson = lessonRepository.findById(quizDTO.getLessonId()).orElseThrow(
                    () -> new NotFoundException("Lesson not found by id " + quizDTO.getLessonId())
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

//            Lesson lesson = lessonRepository.findById(quizDTO.getLessonId()).orElseThrow(
//                    () -> new NotFoundException("Lesson not found by id " + quizDTO.getLessonId())
//            );

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
//            if(lesson != null) {
//                quiz.setLesson(lesson);
//            }
            if (quizDTO.getQuestions() != null && !quizDTO.getQuestions().isEmpty()) {
                List<Question> questionList = questionRepository.findAllById(
                        quizDTO.getQuestions().stream().map(Question::getId).toList()
                );

                if (questionList.size() != quizDTO.getQuestions().size()) {
                    throw new NotFoundException("One or more questions not found !");
                }

                quiz.setQuestions(questionList);
            }
            quiz.setUpdateDate(LocalDateTime.now());

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
    public APIResponse restoreQuiz(Long id) {
        APIResponse apiResponse = new APIResponse();

        Quiz quiz = quizRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Quiz not found by id " + id)
        );

        quiz.setDeleted(false);
        quizRepository.save(quiz);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Restore quiz success !");
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
    public APIResponse getAllQuizzessByLessonId(Long lessonId) {
        APIResponse apiResponse = new APIResponse();

        List<Quiz> quizzes = quizRepository.findByLessonId(lessonId);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get all lessons by lesson id success !");
        apiResponse.setData(quizzes);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    
    @Override
    public APIResponse getQuizByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<Quiz> quizzes = quizRepository.findAll(pageable);

        Page<QuizDTO> quizDTOS = quizzes.map(quiz -> {
            QuizDTO quizDTO = new QuizDTO();
            quizDTO.setId(quiz.getId());
            quizDTO.setQuizEnum(quiz.getQuizEnum());
            quizDTO.setQuizName(quiz.getQuizName());
            quizDTO.setPrice(quiz.getPrice());
            quizDTO.setDescription(quiz.getDescription());
            quizDTO.setLessonId(quiz.getLesson().getId());
            quizDTO.setDate(quiz.getDate());
            quizDTO.setUpdateDate(quiz.getUpdateDate());
            quizDTO.setLessonName(quiz.getLesson().getLessonName());

            return quizDTO;
        });

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get quiz by page success !");
        apiResponse.setData(quizDTOS);
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

    @Override
    public APIResponse checkIfUserHasDoneQuiz(Long userId, Long quizId) {

        APIResponse apiResponse = new APIResponse();

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(
                () -> new NotFoundException("Quiz not found !")
        );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found !")
        );

        boolean hasDone = quiz.getUserSubmit() != null && quiz.getUserSubmit().contains(user);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage(hasDone ? "User has done quiz !" : "User start quiz !");
        apiResponse.setData(hasDone);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;

    }

    @Override
    public APIResponse submitQuiz(Long id,Long userId, AnswerUserDTO answerUserDTO) {
        APIResponse apiResponse = new APIResponse();
        long point = 0L;

        Quiz quiz = quizRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Quiz not found !")
        );
        List<String> answerCorrect = quiz.getQuestions()
                .stream()
                .map(Question::getAnswerCorrect)
                .toList();

        for(String answerUserDTO1 : answerUserDTO.getAnswersUser()) {
            if(answerCorrect.contains(answerUserDTO1)) {
                point++;
            }
        }

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found !")
        );

        quiz.getUserSubmit().add(user);
        quizRepository.save(quiz);

        userPointHistoryService.addUserPointHistory(new UserPointHistoryDTO(user.getId(),point));
        NotificationDTO notificationDTO = (NotificationDTO) notificationService.sendSystemNotification(userId, "Bạn vừa nhận được "+point+" point từ quiz", "QUIZ", 1L).getData();
        webSocketSender.sendNotification(notificationDTO);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Submit quiz success !");
        apiResponse.setData(point);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getQuizByLessonIdAndByPage(Long lessonId, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<Quiz> quizzes = quizRepository.findByLessonIdAndIsDeletedFalse(lessonId,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get quiz by lesson id success !");
        apiResponse.setData(quizzes);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}
