package com.TrungTinhBackend.codearena_backend.Service.Process;

import com.TrungTinhBackend.codearena_backend.Response.APIResponse;

public interface ProcessService {
    public APIResponse updateProcess(Long userId, Long courseId, Long lessonId) throws Exception;
}
