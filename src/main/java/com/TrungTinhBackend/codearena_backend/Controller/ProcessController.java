package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Process.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @PutMapping("/update/{userId}/{courseId}/{lessonId}")
    public ResponseEntity<APIResponse> updateCourse(@PathVariable Long userId,
                                                    @PathVariable Long courseId,
                                                    @PathVariable Long lessonId) throws Exception {
        return ResponseEntity.ok(processService.updateProcess(userId, courseId, lessonId));
    }
}
