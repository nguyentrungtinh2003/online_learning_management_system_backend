package com.TrungTinhBackend.codearena_backend.Service.Lesson;

import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.Lesson;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Repository.CourseRepository;
import com.TrungTinhBackend.codearena_backend.Repository.LessonRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestLesson;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Video.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

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
        return null;
    }

    @Override
    public APIResponse deleteLesson(Long id) throws Exception {
        return null;
    }
}
