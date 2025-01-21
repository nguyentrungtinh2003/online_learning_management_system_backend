package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestCourse;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestUserRegister;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Course.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addCourse(@Valid @RequestPart(value = "course") APIRequestCourse apiRequestCourse,
                                                 @RequestPart(value = "img") MultipartFile img) throws Exception {
        return ResponseEntity.ok(courseService.addCourse(apiRequestCourse, img));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse> updateCourse(@PathVariable Long id,@Valid @RequestPart(value = "course") APIRequestCourse apiRequestCourse,
                                                 @RequestPart(value = "img") MultipartFile img) throws Exception {
        return ResponseEntity.ok(courseService.updateCourse(id,apiRequestCourse, img));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteCourse(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(courseService.deleteCourse(id));
    }
}
