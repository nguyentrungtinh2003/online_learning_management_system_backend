package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.DTO.QuestionDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Question.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("teacher/questions/add")
    public ResponseEntity<APIResponse> addQuestion(@Valid @RequestPart(value = "question") QuestionDTO questionDTO,
                                               @RequestPart(value = "img",required = false) MultipartFile img) throws Exception {
        return ResponseEntity.ok(questionService.addQuestion(questionDTO, img));
    }

    @GetMapping("teacher/questions/quiz/{quizId}/page")
    public ResponseEntity<APIResponse> getQuestionByQuizAndPage(@PathVariable Long quizId,@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "6") int size) throws Exception {
        return ResponseEntity.ok(questionService.getQuestionByQuizIdAndByPage(quizId,page,size));
    }

    @GetMapping("teacher/questions/all")
    public ResponseEntity<APIResponse> getAllQuestion() {
        return ResponseEntity.ok(questionService.getAllQuestion());
    }

    @GetMapping("questions/{id}")
    public ResponseEntity<APIResponse> getQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }

    @GetMapping("teacher/questions/search")
    public ResponseEntity<APIResponse> searchQuestion(@RequestParam String keyword,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) throws Exception {
        return ResponseEntity.ok(questionService.searchQuestion(keyword, page,size));
    }

    @GetMapping("teacher/questions/page")
    public ResponseEntity<APIResponse> getQuestionByPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size) throws Exception {
        return ResponseEntity.ok(questionService.getQuestionByPage(page,size));
    }

    @PutMapping("teacher/questions/update/{id}")
    public ResponseEntity<APIResponse> updateQuestion(@PathVariable Long id,@Valid @RequestPart(value = "question") QuestionDTO questionDTO,
                                                    @RequestPart(value = "img",required = false) MultipartFile img) throws Exception {
        return ResponseEntity.ok(questionService.updateQuestion(id,questionDTO, img));
    }

    @DeleteMapping("teacher/questions/delete/{id}")
    public ResponseEntity<APIResponse> deleteQuestion(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(questionService.deleteQuestion(id));
    }
}
