package com.TrungTinhBackend.codearena_backend.Service.Process;

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

    public ProcessServiceImpl(ProcessRepository processRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.processRepository = processRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public APIResponse createInitialProcess(Long userId, Long courseId) {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found !")
        );

        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new NotFoundException("Course not found !")
        );

        // 1. Tạo process cho khóa học (lesson = null)
        if (processRepository.findByUserIdAndCourseIdAndLessonIsNull(userId, courseId) == null) {
            Process courseProcess = new Process();
            courseProcess.setUser(user);
            courseProcess.setCourse(course);
            courseProcess.setLesson(null);
            courseProcess.setCompletionPercent(0);
            courseProcess.setStatus(ProcessEnum.IN_PROGRESS);
            processRepository.save(courseProcess);
        }

        List<Long> lessonIds = course.getLessons()
                .stream()
                .map(Lesson::getId)
                .toList();
        // 2. Tạo process cho từng bài học
        for (Long lessonId : lessonIds) {
            if (processRepository.findByUserIdAndLessonId(userId, lessonId) == null) {
                Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
                        () -> new NotFoundException("Lesson not found !")
                );
                Process lessonProcess = new Process();
                lessonProcess.setUser(user);
                lessonProcess.setCourse(course);
                lessonProcess.setLesson(lesson); // bạn cần có class Lesson và mapping
                lessonProcess.setCompletionPercent(0);
                lessonProcess.setStatus(ProcessEnum.IN_PROGRESS);
                processRepository.save(lessonProcess);
            }
        }

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add process success !");
        apiResponse.setData(null);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse updateProcess(Long userId, Long courseId, Long lessonId) throws Exception {
        APIResponse apiResponse = new APIResponse();

            Process lessonProcess = processRepository.findByUserIdAndLessonId(userId, lessonId);
            lessonProcess.setCompletionPercent(100);
            processRepository.save(lessonProcess);

            // 2. Tính số bài đã hoàn thành / tổng số bài
            long totalLessons = processRepository.countByCourseIdAndLessonIsNotNull(courseId);
            long completedLessons = processRepository.countByCourseIdAndUserIdAndCompletionPercent(courseId, userId, 100);

            Process courseProcess = processRepository.findByUserIdAndCourseIdAndLessonIsNull(userId, courseId);

            int newCompletionPercent = (int) ((completedLessons * 100)/totalLessons);
            courseProcess.setCompletionPercent(newCompletionPercent);

            if(newCompletionPercent == 100) {
                courseProcess.setStatus(ProcessEnum.COMPLETED);
            }

            processRepository.save(courseProcess);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Update process success !");
            apiResponse.setData(courseProcess);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
    }

}
