package com.TrungTinhBackend.codearena_backend.Service.Course;

import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Repository.CourseRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestCourse;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.User.UserService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService{

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImgService imgService;

    @Override
    public APIResponse addCourse(APIRequestCourse apiRequestCourse, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {

            User lecturer = userRepository.findById(apiRequestCourse.getUser().getId()).orElseThrow(
                    () -> new RuntimeException("User not found !")
            );

            Course course = new Course();

            course.setCourseName(apiRequestCourse.getCourseName());
            course.setDescription(apiRequestCourse.getDescription());
            course.setPrice(apiRequestCourse.getPrice());
            course.setCourseEnum(apiRequestCourse.getCourseEnum());
            course.setDate(LocalDateTime.now());
            course.setDeleted(false);

            course.setImg(imgService.uploadImg(img));
            course.setUser(lecturer);

            courseRepository.save(course);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Add course success !");
            apiResponse.setData(course);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }

    @Override
    public APIResponse updateCourse(Long id, APIRequestCourse apiRequestCourse, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {

            User lecturer = userRepository.findById(apiRequestCourse.getUser().getId()).orElseThrow(
                    () -> new RuntimeException("User not found !")
            );

            Course course = courseRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("Course not found !")
            );

            if(apiRequestCourse.getCourseName() != null && !apiRequestCourse.getCourseName().isEmpty()) {
                course.setCourseName(apiRequestCourse.getCourseName());
            }
            if(apiRequestCourse.getDescription() != null && !apiRequestCourse.getDescription().isEmpty()) {
                course.setDescription(apiRequestCourse.getDescription());
            }
            if(apiRequestCourse.getPrice() != null && !apiRequestCourse.getPrice().isInfinite()) {
                course.setPrice(apiRequestCourse.getPrice());
            }
            if(apiRequestCourse.getCourseEnum() != null) {
                course.setCourseEnum(apiRequestCourse.getCourseEnum());
            }
            if(img != null && !img.isEmpty()) {
                course.setImg(imgService.updateImg(course.getImg(),img));
            }
            if(lecturer != null) {
                course.setUser(lecturer);
            }
            courseRepository.save(course);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Update course success !");
            apiResponse.setData(course);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }

    @Override
    public APIResponse deleteCourse(Long id) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {

            Course course = courseRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("Course not found !")
            );

            course.setDeleted(true);
            courseRepository.save(course);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Delete course success !");
            apiResponse.setData(course);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }
}
