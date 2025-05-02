package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.DTO.LessonCommentDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.LessonComment.LessonCommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/lesson-comment")
public class LessonCommentController {

    @Autowired
    private LessonCommentService lessonCommentService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addLessonComment(@Valid @RequestBody LessonCommentDTO lessonCommentDTO
                                                ) throws Exception {
        return ResponseEntity.ok(lessonCommentService.addLessonComment(lessonCommentDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllLessonComment() {
        return ResponseEntity.ok(lessonCommentService.getAllLessonComment());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getLessonCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(lessonCommentService.getLessonCommentById(id));
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<APIResponse> getLessonCommentByLessonId(@PathVariable Long lessonId) {
        return ResponseEntity.ok(lessonCommentService.getLessonCommentByLessonId(lessonId));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchLessonComment(@RequestParam(required = false) String keyword,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size) throws Exception {
        return ResponseEntity.ok(lessonCommentService.searchLessonComment(keyword, page,size));
    }

    @GetMapping("/page")
    public ResponseEntity<APIResponse> getLessonCommentByPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size) throws Exception {
        return ResponseEntity.ok(lessonCommentService.getLessonCommentByPage(page,size));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteLessonCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(lessonCommentService.deleteLessonComment(id));
    }
}
