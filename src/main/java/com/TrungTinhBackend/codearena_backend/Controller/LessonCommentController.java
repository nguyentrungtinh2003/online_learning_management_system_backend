package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestLesson;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestLessonComment;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.LessonComment.LessonCommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
}
