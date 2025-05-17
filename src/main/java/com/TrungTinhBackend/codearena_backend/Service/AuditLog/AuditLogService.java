package com.TrungTinhBackend.codearena_backend.Service.AuditLog;

import com.TrungTinhBackend.codearena_backend.Response.APIResponse;

public interface AuditLogService {
    public APIResponse addLog(String username, String action, String details);
    public APIResponse getAllLog();
}
