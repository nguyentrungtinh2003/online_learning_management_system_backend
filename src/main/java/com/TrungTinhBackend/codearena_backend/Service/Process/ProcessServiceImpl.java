package com.TrungTinhBackend.codearena_backend.Service.Process;

import com.TrungTinhBackend.codearena_backend.DTO.UserPointHistoryDTO;
import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.Lesson;
import com.TrungTinhBackend.codearena_backend.Entity.Process;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Enum.ProcessEnum;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.CourseRepository;
import com.TrungTinhBackend.codearena_backend.Repository.LessonRepository;
import com.TrungTinhBackend.codearena_backend.Repository.ProcessRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.User.UserService;
import com.TrungTinhBackend.codearena_backend.Service.UserPointHistory.UserPointHistoryService;
import com.TrungTinhBackend.codearena_backend.Service.WebSocket.WebSocketSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcessServiceImpl implements ProcessService{

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WebSocketSender webSocketSender;

    @Autowired
    private UserPointHistoryService userPointHistoryService;

    public ProcessServiceImpl(ProcessRepository processRepository, UserRepository userRepository, CourseRepository courseRepository, UserService userService, WebSocketSender webSocketSender, UserPointHistoryService userPointHistoryService) {
        this.processRepository = processRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.webSocketSender = webSocketSender;
        this.userPointHistoryService = userPointHistoryService;
    }

    @Override
    public APIResponse createInitialProcess(Long userId, Long courseId) {
        APIResponse apiResponse = new APIResponse();

        // 1. Lấy user và course từ DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found!"));

//        // 2. Tạo process cho khóa học (lesson = null) nếu chưa có
//        Process courseProcess = processRepository.findByUserIdAndCourseIdAndLessonIsNull(userId, courseId);
//        if (courseProcess == null) {
//            courseProcess = new Process();
//            courseProcess.setUser(user);
//            courseProcess.setCourse(course);
//            courseProcess.setLesson(null);
//            courseProcess.setCompletionPercent(0);
//            courseProcess.setStatus(ProcessEnum.IN_PROGRESS);
//            processRepository.save(courseProcess);
//        }

        // 3. Tạo process cho từng bài học nếu chưa có
        List<Lesson> lessons = course.getLessons();
        for (Lesson lesson : lessons) {
            if (processRepository.findByUserIdAndLessonId(userId, lesson.getId()) == null) {
                Process lessonProcess = new Process();
                lessonProcess.setUser(user);
                lessonProcess.setCourse(course);
                lessonProcess.setLesson(lesson);
                lessonProcess.setCompletionPercent(0);
                lessonProcess.setStatus(ProcessEnum.IN_PROGRESS);
                processRepository.save(lessonProcess);
            }
        }

        // 4. Trả về kết quả
        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add process success!");
        apiResponse.setData(null);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse updateProcess(Long userId, Long courseId, Long lessonId) {
        APIResponse apiResponse = new APIResponse();

        // 0. Đảm bảo tất cả Process đều được tạo (bao gồm lesson mới nếu có)
        createInitialProcess(userId, courseId);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found !")
        );

        // 1. Cập nhật tiến độ bài học
        Process lessonProcess = processRepository.findByUserIdAndLessonId(userId, lessonId);
        if (lessonProcess == null) {
            throw new NotFoundException("Lesson process not found!");
        }

        // Nếu bài học chưa hoàn thành thì mới cập nhật
        if (lessonProcess.getCompletionPercent() < 100) {
            lessonProcess.setCompletionPercent(100);
            lessonProcess.setStatus(ProcessEnum.COMPLETED);

           userPointHistoryService.addUserPointHistory(new UserPointHistoryDTO(userId,1L));

            processRepository.saveAndFlush(lessonProcess);
        }

        // 2. Tính tổng số bài học và số bài đã hoàn thành
        long totalLessons = processRepository.countByCourseIdAndUserIdAndLessonIsNotNull(courseId, userId);
        long completedLessons = processRepository.countByCourseIdAndUserIdAndLessonIsNotNullAndCompletionPercent(courseId, userId, 100);

        // 3. Cập nhật tiến độ khóa học (lesson = null)
        Process courseProcess = processRepository.findByUserIdAndCourseIdAndLessonIsNull(userId, courseId);
        if (courseProcess == null) {
            throw new NotFoundException("Course process not found!");
        }

        int newCompletionPercent = (int) ((completedLessons * 100) / totalLessons);
        courseProcess.setCompletionPercent(newCompletionPercent);
        courseProcess.setStatus(newCompletionPercent == 100 ? ProcessEnum.COMPLETED : ProcessEnum.IN_PROGRESS);

        processRepository.save(courseProcess);



        // 4. Trả về kết quả
        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Update process success!");
        apiResponse.setData(courseProcess);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

}
