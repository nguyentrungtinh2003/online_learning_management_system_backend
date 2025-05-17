package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.DTO.CourseDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.AuditLog.AuditLogService;
import com.TrungTinhBackend.codearena_backend.Service.Course.CourseService;
import com.TrungTinhBackend.codearena_backend.Utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/")
public class CourseController {

    private final CourseService courseService;
    private final AuditLogService auditLogService;

    @Autowired
    public CourseController(CourseService courseService, AuditLogService auditLogService) {
        this.courseService = courseService;
        this.auditLogService = auditLogService;
    }

    String username = SecurityUtils.getCurrentUsername();

    @PostMapping("teacher/courses/add")
    public ResponseEntity<APIResponse> addCourse(@Valid @RequestPart(value = "course") CourseDTO courseDTO,
                                                 @RequestPart(value = "img",required = false) MultipartFile img) throws Exception {
        auditLogService.addLog(username,"ADD","Add course");
        return ResponseEntity.ok(courseService.addCourse(courseDTO, img));
    }

    @PostMapping("/courses/buy/{userId}/{courseId}")
    public ResponseEntity<APIResponse> buyCourse(@PathVariable Long userId,
                                                 @PathVariable Long courseId) throws Exception {
        auditLogService.addLog(username,"BUY","Buy course "+courseId);
        return ResponseEntity.ok(courseService.buyCourse(userId, courseId));
    }

    @GetMapping("/courses/all")
    public ResponseEntity<APIResponse> getAllCourse() {
        return ResponseEntity.ok(courseService.getAllCourse());
    }

    @GetMapping("/courses/count")
    public ResponseEntity<APIResponse> getCountCourse() {
        return ResponseEntity.ok(courseService.countCourse());
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<APIResponse> getCourseById(@PathVariable Long id) {
        auditLogService.addLog(username,"VIEW","View course "+id);
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/user/{userId}/courses-progress")
    public ResponseEntity<APIResponse> getCoursesProgress(@PathVariable Long userId) {
        return ResponseEntity.ok(courseService.getCoursesProgress(userId));
    }

    @GetMapping("teacher/courses/user/{userId}")
    public ResponseEntity<APIResponse> getCourseByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(courseService.getCourseByUserId(userId));
    }

    @GetMapping("teacher/courses/search")
    public ResponseEntity<APIResponse> searchCourse(@RequestParam(required = false) String keyword,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size) throws Exception {
        auditLogService.addLog(username,"SEARCH","Search course keyword  "+keyword);
        return ResponseEntity.ok(courseService.searchCourse(keyword, page,size));
    }

    @GetMapping("teacher/courses/page")
    public ResponseEntity<APIResponse> getCourseByPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size) throws Exception {
        return ResponseEntity.ok(courseService.getCourseByPage(page,size));
    }

    @PutMapping("teacher/courses/update/{id}")
    public ResponseEntity<APIResponse> updateCourse(@PathVariable Long id,@Valid @RequestPart(value = "course") CourseDTO courseDTO,
                                                 @RequestPart(value = "img",required = false) MultipartFile img) throws Exception {
        auditLogService.addLog(username,"UPDATE","Update course "+id);
        return ResponseEntity.ok(courseService.updateCourse(id,courseDTO, img));
    }

    @DeleteMapping("teacher/courses/delete/{id}")
    public ResponseEntity<APIResponse> deleteCourse(@PathVariable Long id) throws Exception {
        auditLogService.addLog(username,"DELETE","Delete course "+id);
        return ResponseEntity.ok(courseService.deleteCourse(id));
    }

    @PutMapping("teacher/courses/restore/{id}")
    public ResponseEntity<APIResponse> restoreCourse(@PathVariable Long id) throws Exception {
        auditLogService.addLog(username,"RESTORE","Restore course "+id);
        return ResponseEntity.ok(courseService.restoreCourse(id));
    }
}
