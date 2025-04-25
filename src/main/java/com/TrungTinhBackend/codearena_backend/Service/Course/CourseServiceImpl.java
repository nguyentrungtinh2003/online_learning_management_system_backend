package com.TrungTinhBackend.codearena_backend.Service.Course;

import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.CourseRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.DTO.CourseDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Enrollment.EnrollmentService;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.CourseSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService{

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private ImgService imgService;

    public CourseServiceImpl(CourseRepository courseRepository, UserRepository userRepository, EnrollmentService enrollmentService, ImgService imgService) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.enrollmentService = enrollmentService;
        this.imgService = imgService;
    }

    @Override
    public APIResponse addCourse(CourseDTO courseDTO, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();

            User lecturer = userRepository.findById(courseDTO.getUser().getId()).orElseThrow(
                    () -> new NotFoundException("User not found !")
            );

            Course course = new Course();

            course.setCourseName(courseDTO.getCourseName());
            course.setDescription(courseDTO.getDescription());
            course.setPrice(courseDTO.getPrice());
            course.setCourseEnum(courseDTO.getCourseEnum());
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
    }

    @Override
    public APIResponse buyCourse(Long userId, Long courseId) {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found by id " + userId)
        );

        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new NotFoundException("Course not found by id " + courseId)
        );

        if(course.getPrice() > 0) {
            if(user.getCoin() < course.getPrice()) {
                apiResponse.setStatusCode(500L);
                apiResponse.setMessage("Bạn không đủ coin để mua !");
                apiResponse.setTimestamp(LocalDateTime.now());

                return apiResponse;
            }
            user.setCoin(user.getCoin() - course.getPrice());
            userRepository.save(user);
            enrollmentService.enrollUser(userId,courseId);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("User enroll course success !");
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
        }

        enrollmentService.enrollUser(userId,courseId);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("User enroll course success !");
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getAllCourse() {
        APIResponse apiResponse = new APIResponse();

        List<Course> courses = courseRepository.findByIsDeletedFalse();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get all course success !");
        apiResponse.setData(courses);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getCourseById(Long id) {
        APIResponse apiResponse = new APIResponse();

        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Course not found !")
        );

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get course by id success !");
        apiResponse.setData(course);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse updateCourse(Long id, CourseDTO courseDTO, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();

        User lecturer = userRepository.findById(courseDTO.getUser().getId()).orElseThrow(
                () -> new NotFoundException("User not found !")
        );
            Course course = courseRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Course not found by id " + id)
            );

            if(courseDTO.getCourseName() != null && !courseDTO.getCourseName().isEmpty()) {
                course.setCourseName(courseDTO.getCourseName());
            }
            if(courseDTO.getDescription() != null && !courseDTO.getDescription().isEmpty()) {
                course.setDescription(courseDTO.getDescription());
            }
            if(courseDTO.getPrice() != null && !courseDTO.getPrice().isInfinite()) {
                course.setPrice(courseDTO.getPrice());
            }
            if(courseDTO.getCourseEnum() != null) {
                course.setCourseEnum(courseDTO.getCourseEnum());
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
    }

    @Override
    public APIResponse deleteCourse(Long id) throws Exception {
        APIResponse apiResponse = new APIResponse();

            Course course = courseRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Course not found by id " + id)
            );

            course.setDeleted(true);
            courseRepository.save(course);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Delete course success !");
            apiResponse.setData(course);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
    }

    @Override
    public APIResponse searchCourse(String keyword, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Specification<Course> specification = CourseSpecification.searchByKeyword(keyword);
        Page<Course> courses = courseRepository.findAll(specification,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search course success !");
        apiResponse.setData(courses);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getCourseByUserId(Long userId) {
        APIResponse apiResponse = new APIResponse();

        List<Course> courses = courseRepository.findCourseByUserId(userId);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search course success !");
        apiResponse.setData(courses);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getCourseByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<Course> courses = courseRepository.findByIsDeletedFalse(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get course by page success !");
        apiResponse.setData(courses);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}
