package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestCourse;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestQuiz;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Quiz.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addQuiz(@Valid @RequestPart(value = "quiz") APIRequestQuiz apiRequestQuiz,
                                                 @RequestPart(value = "img") MultipartFile img) throws Exception {
        return ResponseEntity.ok(quizService.addQuiz(apiRequestQuiz, img));
    }
}
