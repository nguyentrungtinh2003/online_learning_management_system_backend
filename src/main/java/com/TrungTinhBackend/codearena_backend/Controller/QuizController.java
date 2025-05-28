package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.DTO.AnswerUserDTO;
import com.TrungTinhBackend.codearena_backend.DTO.QuizDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.AuditLog.AuditLogService;
import com.TrungTinhBackend.codearena_backend.Service.Quiz.QuizService;
import com.TrungTinhBackend.codearena_backend.Utils.SecurityUtils;
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

    @Autowired
    private AuditLogService auditLogService;

    @PostMapping("teacher/quizzes/add")
    public ResponseEntity<APIResponse> addQuiz(@Valid @RequestPart(value = "quiz") QuizDTO quizDTO,
                                                 @RequestPart(value = "img", required = false) MultipartFile img) throws Exception {
        String username = SecurityUtils.getCurrentUsername();
        auditLogService.addLog(username,"ADD","Add quiz");
        return ResponseEntity.ok(quizService.addQuiz(quizDTO, img));
    }

    @PostMapping("/quizzes/submit/{id}/{userId}")
    public ResponseEntity<APIResponse> submitQuiz(@PathVariable Long id,
                                                  @PathVariable Long userId,
                                                  @RequestBody AnswerUserDTO answerUserDTO) throws Exception {
        String username = SecurityUtils.getCurrentUsername();
        auditLogService.addLog(username,"SUBMIT","Submit quiz "+id);
        return ResponseEntity.ok(quizService.submitQuiz(id,userId, answerUserDTO));
    }

    @GetMapping("/quizzes/check/{userId}/{quizId}")
    public ResponseEntity<APIResponse> checkIfUserHasDoneQuiz(@PathVariable Long userId,
                                                  @PathVariable Long quizId) throws Exception {
        return ResponseEntity.ok(quizService.checkIfUserHasDoneQuiz(userId, quizId));
    }

    @GetMapping("teacher/quizzes/lesson/{lessonId}/page")
    public ResponseEntity<APIResponse> getQuizByLessonAndPage(@PathVariable Long lessonId,@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "6") int size) throws Exception {
        return ResponseEntity.ok(quizService.getQuizByLessonIdAndByPage(lessonId,page,size));
    }

    @GetMapping("teacher/quizzes/all")
    public ResponseEntity<APIResponse> getAllQuiz() {
        return ResponseEntity.ok(quizService.getAllQuiz());
    }

    @GetMapping("/quizzes/lessons/{lessonId}/all")
    public ResponseEntity<APIResponse> getAllQuizzessByLessonId(@PathVariable Long lessonId) {
        return ResponseEntity.ok(quizService.getAllQuizzessByLessonId(lessonId));
    }

    @GetMapping("quizzes/{id}")
    public ResponseEntity<APIResponse> getQuizById(@PathVariable Long id) {
        String username = SecurityUtils.getCurrentUsername();
        auditLogService.addLog(username,"VIEW","View quiz "+id);
        return ResponseEntity.ok(quizService.getQuizById(id));
    }

    @GetMapping("teacher/quizzes/search")
    public ResponseEntity<APIResponse> searchQuiz(@RequestParam String keyword,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) throws Exception {
        String username = SecurityUtils.getCurrentUsername();
        auditLogService.addLog(username,"SEARCH","Search quiz keyword "+keyword);
        return ResponseEntity.ok(quizService.searchQuiz(keyword, page,size));
    }

    @GetMapping("teacher/quizzes/page")
    public ResponseEntity<APIResponse> getQuizByPage(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "6") int size) throws Exception {
        return ResponseEntity.ok(quizService.getQuizByPage(page,size));
    }

    @PutMapping("teacher/quizzes/update/{id}")
    public ResponseEntity<APIResponse> updateQuiz(@PathVariable Long id, @Valid @RequestPart(value = "quiz") QuizDTO quizDTO,
                                               @RequestPart(value = "img",required = false) MultipartFile img) throws Exception {
        String username = SecurityUtils.getCurrentUsername();
        auditLogService.addLog(username,"UPDATE","Update quiz "+id);
        return ResponseEntity.ok(quizService.updateQuiz(id, quizDTO, img));
    }

    @DeleteMapping("teacher/quizzes/delete/{id}")
    public ResponseEntity<APIResponse> deleteQuiz(@PathVariable Long id) throws Exception {
        String username = SecurityUtils.getCurrentUsername();
        auditLogService.addLog(username,"DELETE","Delete quiz "+id);
        return ResponseEntity.ok(quizService.deleteQuiz(id));
    }

    @PutMapping("teacher/quizzes/restore/{id}")
    public ResponseEntity<APIResponse> restoreQuiz(@PathVariable Long id) throws Exception {
        String username = SecurityUtils.getCurrentUsername();
        auditLogService.addLog(username,"RESTORE","Restore quiz "+id);
        return ResponseEntity.ok(quizService.restoreQuiz(id));
    }
}
