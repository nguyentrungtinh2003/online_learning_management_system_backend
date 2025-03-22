package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Repository.CourseRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestCourse;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestUserRegister;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Course.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @PreAuthorize("hasAuthority('ADMIN','TEACHER')")
    @PostMapping("teacher/courses/add")
    public ResponseEntity<APIResponse> addCourse(@Valid @RequestPart(value = "course") APIRequestCourse apiRequestCourse,
                                                 @RequestPart(value = "img",required = false) MultipartFile img) throws Exception {
        return ResponseEntity.ok(courseService.addCourse(apiRequestCourse, img));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("admin/courses/all")
    public ResponseEntity<APIResponse> getAllCourse() {
        return ResponseEntity.ok(courseService.getAllCourse());
    }

    @PreAuthorize("hasAuthority('ADMIN','TEACHER')")
    @GetMapping("teacher/courses/{id}")
    public ResponseEntity<APIResponse> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PreAuthorize("hasAuthority('ADMIN','TEACHER')")
    @GetMapping("teacher/courses/user/{userId}")
    public ResponseEntity<APIResponse> getCourseByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(courseService.getCourseByUserId(userId));
    }

    @PreAuthorize("hasAuthority('ADMIN','TEACHER')")
    @GetMapping("teacher/courses/search")
    public ResponseEntity<APIResponse> searchCourse(@RequestParam(required = false) String keyword,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size) throws Exception {
        return ResponseEntity.ok(courseService.searchCourse(keyword, page,size));
    }

    @PreAuthorize("hasAuthority('ADMIN','TEACHER')")
    @GetMapping("teacher/courses/page")
    public ResponseEntity<APIResponse> getCourseByPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size) throws Exception {
        return ResponseEntity.ok(courseService.getCourseByPage(page,size));
    }

    @PreAuthorize("hasAuthority('ADMIN','TEACHER')")
    @PutMapping("teacher/courses/update/{id}")
    public ResponseEntity<APIResponse> updateCourse(@PathVariable Long id,@Valid @RequestPart(value = "course") APIRequestCourse apiRequestCourse,
                                                 @RequestPart(value = "img",required = false) MultipartFile img) throws Exception {
        return ResponseEntity.ok(courseService.updateCourse(id,apiRequestCourse, img));
    }

    @PreAuthorize("hasAuthority('ADMIN','TEACHER')")
    @DeleteMapping("teacher/courses/delete/{id}")
    public ResponseEntity<APIResponse> deleteCourse(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(courseService.deleteCourse(id));
    }
}
