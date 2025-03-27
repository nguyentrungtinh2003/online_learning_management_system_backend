package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestCourse;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestQuiz;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Quiz.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("teacher/quizzes/add")
    public ResponseEntity<APIResponse> addQuiz(@Valid @RequestPart(value = "quiz") APIRequestQuiz apiRequestQuiz,
                                                 @RequestPart(value = "img") MultipartFile img) throws Exception {
        return ResponseEntity.ok(quizService.addQuiz(apiRequestQuiz, img));
    }

    @GetMapping("teacher/quizzes/all")
    public ResponseEntity<APIResponse> getAllQuiz() {
        return ResponseEntity.ok(quizService.getAllQuiz());
    }

    @GetMapping("quizzes/{id}")
    public ResponseEntity<APIResponse> getQuizById(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getQuizById(id));
    }

    @GetMapping("teacher/quizzes/search")
    public ResponseEntity<APIResponse> searchQuiz(@RequestParam String keyword,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) throws Exception {
        return ResponseEntity.ok(quizService.searchQuiz(keyword, page,size));
    }

    @GetMapping("teacher/quizzes/page")
    public ResponseEntity<APIResponse> getQuizByPage(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "6") int size) throws Exception {
        return ResponseEntity.ok(quizService.getQuizByPage(page,size));
    }

    @PutMapping("teacher/quizzes/update/{id}")
    public ResponseEntity<APIResponse> updateQuiz(@PathVariable Long id, @Valid @RequestPart(value = "quiz") APIRequestQuiz apiRequestQuiz,
                                               @RequestPart(value = "img") MultipartFile img) throws Exception {
        return ResponseEntity.ok(quizService.updateQuiz(id, apiRequestQuiz, img));
    }

    @DeleteMapping("teacher/quizzes/delete/{id}")
    public ResponseEntity<APIResponse> deleteQuiz(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(quizService.deleteQuiz(id));
    }
}
