package com.TrungTinhBackend.codearena_backend.Service.LoginLog;

import com.TrungTinhBackend.codearena_backend.Entity.Lesson;
import com.TrungTinhBackend.codearena_backend.Entity.LoginLog;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.LoginLogRepository;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoginLogServiceImpl implements LoginLogService{

    private final LoginLogRepository loginLogRepository;

    public LoginLogServiceImpl(LoginLogRepository loginLogRepository) {
        this.loginLogRepository = loginLogRepository;
    }

    @Override
    public void save(LoginLog loginLog) {
        LoginLog loginLog1 = new LoginLog();

        loginLog1.setUsername(loginLog.getUsername());
        loginLog1.setIpAddress(loginLog.getIpAddress());
        loginLog1.setMessage(loginLog.getMessage());
        loginLog1.setSuccess(loginLog.isSuccess());
        loginLog1.setTimestamp(LocalDateTime.now());

        loginLogRepository.save(loginLog1);
    }

    @Override
    public APIResponse getAllLoginLog() {
        APIResponse apiResponse = new APIResponse();

        List<LoginLog> loginLogs = loginLogRepository.findAll();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get login log success !");
        apiResponse.setData(loginLogs);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}


