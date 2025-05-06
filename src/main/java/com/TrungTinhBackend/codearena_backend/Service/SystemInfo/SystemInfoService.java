package com.TrungTinhBackend.codearena_backend.Service.SystemInfo;

import com.TrungTinhBackend.codearena_backend.Entity.SystemInfo;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SystemInfoService {
    public APIResponse addSystemInfo(SystemInfo systemInfo, MultipartFile img) throws IOException;
    public APIResponse getSystemInfo(Long id);
    public APIResponse updateSystemInfo(Long id,SystemInfo systemInfo, MultipartFile img) throws IOException;
    public APIResponse deleteSystemInfo(Long id);
}
