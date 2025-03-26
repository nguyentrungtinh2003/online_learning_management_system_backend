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

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addQuestion(@Valid @RequestPart(value = "question") APIRequestQuestion apiRequestQuestion,
                                               @RequestPart(value = "img") MultipartFile img) throws Exception {
        return ResponseEntity.ok(questionService.addQuestion(apiRequestQuestion, img));
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllQuestion() {
        return ResponseEntity.ok(questionService.getAllQuestion());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchQuestion(@RequestParam String keyword,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) throws Exception {
        return ResponseEntity.ok(questionService.searchQuestion(keyword, page,size));
    }

    @GetMapping("/page")
    public ResponseEntity<APIResponse> getQuestionByPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size) throws Exception {
        return ResponseEntity.ok(questionService.getQuestionByPage(page,size));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse> updateQuestion(@PathVariable Long id,@Valid @RequestPart(value = "question") APIRequestQuestion apiRequestQuestion,
                                                    @RequestPart(value = "img") MultipartFile img) throws Exception {
        return ResponseEntity.ok(questionService.updateQuestion(id,apiRequestQuestion, img));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteQuestion(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(questionService.deleteQuestion(id));
    }
}
