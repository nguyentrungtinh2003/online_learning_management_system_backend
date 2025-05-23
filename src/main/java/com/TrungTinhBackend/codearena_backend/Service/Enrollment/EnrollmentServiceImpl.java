package com.TrungTinhBackend.codearena_backend.Service.Enrollment;

import com.TrungTinhBackend.codearena_backend.DTO.EnrollmentDTO;
import com.TrungTinhBackend.codearena_backend.DTO.NotificationDTO;
import com.TrungTinhBackend.codearena_backend.DTO.TopCourseDTO;
import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.Enrollment;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Enum.EnrollmentStatus;
import com.TrungTinhBackend.codearena_backend.Enum.NotificationStatus;
import com.TrungTinhBackend.codearena_backend.Enum.NotificationType;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.CourseRepository;
import com.TrungTinhBackend.codearena_backend.Repository.EnrollmentRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Notification.NotificationService;
import com.TrungTinhBackend.codearena_backend.Service.Process.ProcessService;
import com.TrungTinhBackend.codearena_backend.Service.WebSocket.WebSocketSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService{

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProcessService processService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private WebSocketSender webSocketSender;


    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, UserRepository userRepository, CourseRepository courseRepository, ProcessService processService, NotificationService notificationService, WebSocketSender webSocketSender) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.processService = processService;
        this.notificationService = notificationService;
        this.webSocketSender = webSocketSender;
    }

    @Override
    public APIResponse enrollUser(Long userId, Long courseId) {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new NotFoundException("Course not found")
        );

        if(enrollmentRepository.existsByUserAndCourse(user,course)) {
            apiResponse.setStatusCode(500L);
            apiResponse.setMessage("User already enrolled!");
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setEnrolledDate(LocalDateTime.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setProgress(0);

        enrollmentRepository.save(enrollment);

        processService.createInitialProcess(userId,courseId);

        NotificationDTO notificationDTO = (NotificationDTO) notificationService.sendSystemNotification(userId,"Bạn vừa mua khoá học "+course.getCourseName()+" Giá "+course.getPrice(), "COURSE", 1L).getData();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("User enroll success !");
        apiResponse.setData(enrollment);
        apiResponse.setTimestamp(LocalDateTime.now());

        webSocketSender.sendNotification(notificationDTO);
        webSocketSender.sendUserInfo(user);

        return apiResponse;
    }

    @Override
    public APIResponse getEnrollByUserId(Long userId) {
        APIResponse apiResponse = new APIResponse();

        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);

        List<EnrollmentDTO> enrollmentDTOS = enrollments.stream().map(enrollment -> {
            EnrollmentDTO enrollmentDTO = new EnrollmentDTO();
            enrollmentDTO.setId(enrollment.getId());
            enrollmentDTO.setCourseName(enrollment.getCourse().getCourseName());
            enrollmentDTO.setUserId(enrollment.getUser().getId());
            enrollmentDTO.setCourseId(enrollment.getCourse().getId());
            enrollmentDTO.setUsername(enrollment.getUser().getUsername());
            enrollmentDTO.setEnrolledDate(enrollment.getEnrolledDate());
            enrollmentDTO.setProgress(enrollment.getProgress());
            enrollmentDTO.setStatus(enrollment.getStatus().toString());
            enrollmentDTO.setDescription(enrollment.getCourse().getDescription());
            enrollmentDTO.setImg(enrollment.getCourse().getImg());
            return enrollmentDTO;
        }).toList();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get user enroll course success !");
        apiResponse.setData(enrollmentDTOS);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getTopEnrolledCourses() {
        APIResponse response = new APIResponse();

        Page<Object[]> result = enrollmentRepository.findTopCoursesByEnrollment(PageRequest.of(0, 4));

        List<TopCourseDTO> topCourses = new ArrayList<>();

        for (Object[] row : result.getContent()) {
            Course course = (Course) row[0];
            Long enrollmentCount = (Long) row[1];

            // Create TopCourseDTO and add to the list
            TopCourseDTO topCourseDTO = new TopCourseDTO(course, enrollmentCount);
            topCourses.add(topCourseDTO);
        }

        response.setStatusCode(200L);
        response.setMessage("Top 4 most enrolled courses");
        response.setData(topCourses);  // Set the mapped list of DTOs
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

}
