package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestCourse;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestQuestion;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestQuiz;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Question.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addQuestion(@Valid @RequestPart(value = "question") APIRequestQuestion apiRequestQuestion,
                                               @RequestPart(value = "img") MultipartFile img) throws Exception {
        return ResponseEntity.ok(questionService.addQuestion(apiRequestQuestion, img));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse> updateQuestion(@PathVariable Long id,@Valid @RequestPart(value = "question") APIRequestQuestion apiRequestQuestion,
                                                    @RequestPart(value = "img") MultipartFile img) throws Exception {
        return ResponseEntity.ok(questionService.updateQuestion(id,apiRequestQuestion, img));
    }
}
