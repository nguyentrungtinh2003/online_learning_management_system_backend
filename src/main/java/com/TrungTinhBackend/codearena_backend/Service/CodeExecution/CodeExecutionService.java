package com.TrungTinhBackend.codearena_backend.Service.CodeExecution;

import com.TrungTinhBackend.codearena_backend.DTO.CodeExecutionDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface CodeExecutionService {
    public APIResponse executeCode(CodeExecutionDTO codeExecutionDTO) throws JsonProcessingException;
    public APIResponse getExecuteCodeById(Long id);
    public APIResponse getExecuteCodeByUserId(Long userId);
    public APIResponse updateCodeExecution(Long id,CodeExecutionDTO codeExecutionDTO );
    public APIResponse deleteCodeExecution(Long id);
}
