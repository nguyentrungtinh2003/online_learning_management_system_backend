package com.TrungTinhBackend.codearena_backend.Service.Enrollment;

import com.TrungTinhBackend.codearena_backend.DTO.EnrollmentDTO;
import com.TrungTinhBackend.codearena_backend.DTO.NotificationDTO;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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


        webSocketSender.sendNotification(notificationDTO);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("User enroll success !");
        apiResponse.setData(enrollment);
        apiResponse.setTimestamp(LocalDateTime.now());
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
}
