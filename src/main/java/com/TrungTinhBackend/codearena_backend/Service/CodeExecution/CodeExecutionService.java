package com.TrungTinhBackend.codearena_backend.Service.CodeExecution;

import com.TrungTinhBackend.codearena_backend.DTO.CodeExecutionDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;

public interface CodeExecutionService {
    public APIResponse executeCode(CodeExecutionDTO codeExecutionDTO);
    public APIResponse getExecuteCodeById(Long id);
}
