package com.TrungTinhBackend.codearena_backend.Service.LoginLog;

import com.TrungTinhBackend.codearena_backend.Entity.LoginLog;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;

public interface LoginLogService {
    void save(LoginLog loginLog);
    public APIResponse getAllLoginLog();
}
