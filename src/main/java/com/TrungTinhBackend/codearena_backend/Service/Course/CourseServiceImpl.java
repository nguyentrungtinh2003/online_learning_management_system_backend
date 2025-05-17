package com.TrungTinhBackend.codearena_backend.Service.Course;

import com.TrungTinhBackend.codearena_backend.DTO.CourseProgressDTO;
import com.TrungTinhBackend.codearena_backend.DTO.EnrollmentDTO;
import com.TrungTinhBackend.codearena_backend.DTO.NotificationDTO;
import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.Lesson;
import com.TrungTinhBackend.codearena_backend.Entity.Process;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Enum.NotificationStatus;
import com.TrungTinhBackend.codearena_backend.Enum.NotificationType;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.CourseRepository;
import com.TrungTinhBackend.codearena_backend.Repository.LessonRepository;
import com.TrungTinhBackend.codearena_backend.Repository.ProcessRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.DTO.CourseDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Enrollment.EnrollmentService;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Notification.NotificationService;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.CourseSpecification;
import com.TrungTinhBackend.codearena_backend.Service.WebSocket.WebSocketSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private NotificationService notificationService;

    public CourseServiceImpl(CourseRepository courseRepository, UserRepository userRepository, EnrollmentService enrollmentService, ImgService imgService, LessonRepository lessonRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.enrollmentService = enrollmentService;
        this.imgService = imgService;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public APIResponse addCourse(CourseDTO courseDTO, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();

            User lecturer = userRepository.findById(courseDTO.getUserId()).orElseThrow(
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
    public APIResponse countCourse() {
        APIResponse apiResponse = new APIResponse();

        Long countCourse = courseRepository.count();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get count course success !");
        apiResponse.setData(countCourse);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse updateCourse(Long id, CourseDTO courseDTO, MultipartFile img) throws Exception {
        APIResponse apiResponse = new APIResponse();

        User lecturer = userRepository.findById(courseDTO.getUserId()).orElseThrow(
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
            course.setUpdateDate(LocalDateTime.now());

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
        Page<Course> courses = courseRepository.findAll(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get course by page success !");
        apiResponse.setData(courses);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse restoreCourse(Long id) {
        APIResponse apiResponse = new APIResponse();

        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Course not found by id " + id)
        );

        course.setDeleted(false);
        courseRepository.save(course);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Restore course success !");
        apiResponse.setData(course);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getCoursesProgress(Long userId) {
        APIResponse apiResponse = new APIResponse();

        // Retrieve enrolled courses for the user
        List<EnrollmentDTO> enrollmentDTOS =(List<EnrollmentDTO>) enrollmentService.getEnrollByUserId(userId).getData();

        // Map the courses to a list of CourseProgressDTO
        List<CourseProgressDTO> courseProgressList = enrollmentDTOS.stream().map(enroll -> {
            List<Lesson> lessons = lessonRepository.findByCourseId(enroll.getCourseId());
            List<Process> processes = processRepository.findByUserIdAndCourseId(userId, enroll.getCourseId());

            long completedLessons = lessons.stream()
                    .filter(lesson -> processes.stream()
                            .anyMatch(p ->p.getLesson() != null && p.getLesson().getId() != null && p.getLesson().getId().equals(lesson.getId()) && p.getCompletionPercent() >= 100))
                    .count();

            int totalLessons = lessons.size();
            int percent = totalLessons > 0 ? (int) ((completedLessons * 100.0) / totalLessons) : 0;

            // Return CourseProgressDTO with the calculated progress
            return new CourseProgressDTO(enroll.getId(), enroll.getCourseName(),enroll.getDescription(),enroll.getImg(), (int) completedLessons, totalLessons, percent);
        }).collect(Collectors.toList());

        // Set the API response details
        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Retrieve course progress success!");
        apiResponse.setData(courseProgressList); // Set the data to the course progress list
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

}
