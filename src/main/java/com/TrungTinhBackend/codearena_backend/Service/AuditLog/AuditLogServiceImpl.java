package com.TrungTinhBackend.codearena_backend.Service.AuditLog;

import com.TrungTinhBackend.codearena_backend.Entity.AuditLog;
import com.TrungTinhBackend.codearena_backend.Entity.Blog;
import com.TrungTinhBackend.codearena_backend.Repository.AuditLogRepository;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.BlogSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService{

    @Autowired
    private AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public APIResponse addLog(String username, String action, String details) {
        APIResponse apiResponse = new APIResponse();

        AuditLog auditLog = new AuditLog();
        auditLog.setUsername(username);
        auditLog.setAction(action);
        auditLog.setDetails(details);
        auditLog.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(auditLog);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Save log success !");
        apiResponse.setData(auditLog);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getAllLog() {
        APIResponse apiResponse = new APIResponse();

        List<AuditLog> auditLogs = auditLogRepository.findAll();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get all log success !");
        apiResponse.setData(auditLogs);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}
