package com.TrungTinhBackend.codearena_backend.Service.Lesson;

import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.Lesson;
import com.TrungTinhBackend.codearena_backend.Entity.Quiz;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Repository.CourseRepository;
import com.TrungTinhBackend.codearena_backend.Repository.LessonRepository;
import com.TrungTinhBackend.codearena_backend.Repository.QuizRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestLesson;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Video.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LessonServiceImpl implements LessonService{

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ImgService imgService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Override
    public APIResponse addLesson(APIRequestLesson apiRequestLesson, MultipartFile img, MultipartFile video) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {
            Course course = courseRepository.findById(apiRequestLesson.getCourse().getId()).orElseThrow(
                    () -> new RuntimeException("Course not found !")
            );
            Lesson lesson = new Lesson();

            lesson.setLessonName(apiRequestLesson.getLessonName());
            lesson.setDescription(apiRequestLesson.getDescription());
            lesson.setDate(LocalDateTime.now());
            lesson.setImg(imgService.uploadImg(img));
            lesson.setVideoURL(videoService.uploadVideo(video));
            lesson.setDeleted(false);
            lesson.setCourse(course);
            lesson.setQuizzes(null);

            lessonRepository.save(lesson);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Add lesson success !");
            apiResponse.setData(lesson);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }

    @Override
    public APIResponse updateLesson(Long id, APIRequestLesson apiRequestLesson, MultipartFile img, MultipartFile video) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {
            Course course = courseRepository.findById(apiRequestLesson.getCourse().getId()).orElseThrow(
                    () -> new RuntimeException("Course not found !")
            );

            Lesson lesson = lessonRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("Lesson not found !")
            );

            if(apiRequestLesson.getLessonName() != null && !apiRequestLesson.getLessonName().isEmpty()) {
                lesson.setLessonName(apiRequestLesson.getLessonName());
            }
            if(apiRequestLesson.getDescription() != null && !apiRequestLesson.getDescription().isEmpty()) {
                lesson.setDescription(apiRequestLesson.getDescription());
            }
            if(img != null && !img.isEmpty()) {
                lesson.setImg(imgService.updateImg(lesson.getImg(),img));
            }
            if(video != null && !video.isEmpty()) {
                lesson.setVideoURL(videoService.updateVideo(lesson.getVideoURL(),video));
            }
            if(apiRequestLesson.getCourse() != null) {
                lesson.setCourse(course);
            }
            if (apiRequestLesson.getQuizzes() != null && !apiRequestLesson.getQuizzes().isEmpty()) {
                List<Quiz> quizList = quizRepository.findAllById(
                        apiRequestLesson.getQuizzes().stream().map(Quiz::getId).toList()
                );

                if (quizList.size() != apiRequestLesson.getQuizzes().size()) {
                    throw new RuntimeException("One or more quizzes not found!");
                }

                lesson.setQuizzes(quizList);
            }

            lessonRepository.save(lesson);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Update lesson success !");
            apiResponse.setData(lesson);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }

    @Override
    public APIResponse deleteLesson(Long id) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {

            Lesson lesson = lessonRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("Lesson not found !")
            );

            lesson.setDeleted(true);
            lessonRepository.save(lesson);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Delete lesson success !");
            apiResponse.setData(lesson);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }
}
