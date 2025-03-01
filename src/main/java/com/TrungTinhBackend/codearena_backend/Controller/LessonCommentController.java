package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestLesson;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestLessonComment;
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
    public ResponseEntity<APIResponse> addLessonComment(@Valid @RequestPart(value = "lessonComment") APIRequestLessonComment apiRequestLessonComment,
                                                 @RequestPart(value = "img",required = false) MultipartFile img,
                                                 @RequestPart(value = "video",required = false) MultipartFile video) throws Exception {
        return ResponseEntity.ok(lessonCommentService.addLessonComment(apiRequestLessonComment, img, video));
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllLessonComment() {
        return ResponseEntity.ok(lessonCommentService.getAllLessonComment());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getLessonCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(lessonCommentService.getLessonCommentById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteLessonCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(lessonCommentService.deleteLessonComment(id));
    }
}
