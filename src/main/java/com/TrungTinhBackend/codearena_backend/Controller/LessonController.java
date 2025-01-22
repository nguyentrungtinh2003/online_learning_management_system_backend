package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestCourse;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestLesson;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Lesson.LessonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addLesson(@Valid @RequestPart(value = "lesson") APIRequestLesson apiRequestLesson,
                                                 @RequestPart(value = "img") MultipartFile img,
                                                 @RequestPart(value = "video") MultipartFile video) throws Exception {
        return ResponseEntity.ok(lessonService.addLesson(apiRequestLesson, img, video));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse> updateLesson(@PathVariable Long id, @Valid @RequestPart(value = "lesson") APIRequestLesson apiRequestLesson,
                                                 @RequestPart(value = "img") MultipartFile img,
                                                 @RequestPart(value = "video") MultipartFile video) throws Exception {
        return ResponseEntity.ok(lessonService.updateLesson(id,apiRequestLesson, img, video));
    }
}
