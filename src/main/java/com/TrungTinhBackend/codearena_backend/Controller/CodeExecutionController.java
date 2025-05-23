package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.DTO.ChatDTO;
import com.TrungTinhBackend.codearena_backend.DTO.CodeExecutionDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.CodeExecution.CodeExecutionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/code")
public class CodeExecutionController {

    @Autowired
    private CodeExecutionService codeExecutionService;

    @PostMapping("/compiler")
    public ResponseEntity<APIResponse> executeCode(@RequestBody CodeExecutionDTO codeExecutionDTO) throws Exception {
        return ResponseEntity.ok(codeExecutionService.executeCode(codeExecutionDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getExecuteCodeById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(codeExecutionService.getExecuteCodeById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<APIResponse> getExecuteCodeByUserId(@PathVariable Long userId) throws Exception {
        return ResponseEntity.ok(codeExecutionService.getExecuteCodeByUserId(userId));
    }

    @PutMapping("/user/update/{id}")
    public ResponseEntity<APIResponse> updateExecuteCodeByUserId(@PathVariable Long id,
                                                                 @RequestBody CodeExecutionDTO codeExecutionDTO) throws Exception {
        return ResponseEntity.ok(codeExecutionService.updateCodeExecution(id,codeExecutionDTO));
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<APIResponse> deleteExecuteCodeByUserId(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(codeExecutionService.deleteCodeExecution(id));
    }
}
