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
@RequestMapping("api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addCourse(@Valid @RequestPart(value = "course") APIRequestCourse apiRequestCourse,
                                                 @RequestPart(value = "img",required = false) MultipartFile img) throws Exception {
        return ResponseEntity.ok(courseService.addCourse(apiRequestCourse, img));
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllCourse() {
        return ResponseEntity.ok(courseService.getAllCourse());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<APIResponse> getCourseByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(courseService.getCourseByUserId(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchCourse(@RequestParam(required = false) String keyword,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size) throws Exception {
        return ResponseEntity.ok(courseService.searchCourse(keyword, page,size));
    }

    @GetMapping("/page")
    public ResponseEntity<APIResponse> getCourseByPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size) throws Exception {
        return ResponseEntity.ok(courseService.getCourseByPage(page,size));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse> updateCourse(@PathVariable Long id,@Valid @RequestPart(value = "course") APIRequestCourse apiRequestCourse,
                                                 @RequestPart(value = "img",required = false) MultipartFile img) throws Exception {
        return ResponseEntity.ok(courseService.updateCourse(id,apiRequestCourse, img));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteCourse(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(courseService.deleteCourse(id));
    }
}
