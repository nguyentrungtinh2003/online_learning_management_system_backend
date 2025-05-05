package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Repository.LessonRepository;
import com.TrungTinhBackend.codearena_backend.DTO.LessonDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Lesson.LessonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private LessonRepository lessonRepository;

    @PostMapping("teacher/lessons/add")
    public ResponseEntity<APIResponse> addLesson(@Valid @RequestPart(value = "lesson") LessonDTO lessonDTO,
                                                 @RequestPart(value = "img",required = false) MultipartFile img,
                                                 @RequestPart(value = "video",required = false) MultipartFile video) throws Exception {
        return ResponseEntity.ok(lessonService.addLesson(lessonDTO, img, video));
    }

    @GetMapping("teacher/lessons/all")
    public ResponseEntity<APIResponse> getAllLesson() {
        return ResponseEntity.ok(lessonService.getAllLesson());
    }

    @GetMapping("lessons/{id}")
    public ResponseEntity<APIResponse> getLessonById(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getLessonById(id));
    }

    @GetMapping("lessons/search")
    public ResponseEntity<APIResponse> searchLesson(@RequestParam(required = false) String keyword,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size) throws Exception {
        return ResponseEntity.ok(lessonService.searchLesson(keyword, page,size));
    }

    @GetMapping("teacher/lessons/page")
    public ResponseEntity<APIResponse> getLessonByPage(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "6") int size) throws Exception {
        return ResponseEntity.ok(lessonService.getLessonByPage(page,size));
    }

    @GetMapping("teacher/lessons/courses/{courseId}/page")
    public ResponseEntity<APIResponse> getLessonByCourseAndPage(@PathVariable Long courseId,@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "6") int size) throws Exception {
        return ResponseEntity.ok(lessonService.getLessonByCourseIdAndByPage(courseId,page,size));
    }

    @PutMapping("teacher/lessons/update/{id}")
    public ResponseEntity<APIResponse> updateLesson(@PathVariable Long id, @Valid @RequestPart(value = "lesson") LessonDTO lessonDTO,
                                                 @RequestPart(value = "img",required = false) MultipartFile img,
                                                 @RequestPart(value = "video",required = false) MultipartFile video) throws Exception {
        return ResponseEntity.ok(lessonService.updateLesson(id,lessonDTO, img, video));
    }

    @GetMapping("teacher/lessons/courses/{courseId}/all")
    public ResponseEntity<APIResponse> getAllLessonsByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(lessonService.getAllLessonsByCourseId(courseId));
    }

    @DeleteMapping("teacher/lessons/delete/{id}")
    public ResponseEntity<APIResponse> deleteLesson(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(lessonService.deleteLesson(id));
    }

    @PutMapping("teacher/lessons/restore/{id}")
    public ResponseEntity<APIResponse> restoreLesson(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(lessonService.restoreLesson(id));
    }
}
