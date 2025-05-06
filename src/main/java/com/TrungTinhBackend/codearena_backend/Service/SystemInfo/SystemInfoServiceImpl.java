package com.TrungTinhBackend.codearena_backend.Service.SystemInfo;

import com.TrungTinhBackend.codearena_backend.Entity.SystemInfo;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.SystemInfoRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SystemInfoServiceImpl implements SystemInfoService{

    @Autowired
    private SystemInfoRepository systemInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImgService imgService;

    public SystemInfoServiceImpl(SystemInfoRepository systemInfoRepository, UserRepository userRepository, ImgService imgService) {
        this.systemInfoRepository = systemInfoRepository;
        this.userRepository = userRepository;
        this.imgService = imgService;
    }

    @Override
    public APIResponse addSystemInfo(SystemInfo systemInfo, MultipartFile img) throws IOException {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findById(systemInfo.getUser().getId()).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        SystemInfo systemInfo1 = new SystemInfo();
        systemInfo1.setSystemName(systemInfo.getSystemName());
        systemInfo1.setDate(LocalDateTime.now());
        systemInfo1.setEmail(systemInfo.getEmail());
        systemInfo1.setAddress(systemInfo.getAddress());
        systemInfo1.setImg(imgService.uploadImg(img));
        systemInfo1.setDeleted(false);
        systemInfo1.setSlogan(systemInfo.getSlogan());
        systemInfo1.setDescription(systemInfo.getDescription());
        systemInfo1.setPhoneNumber(systemInfo.getPhoneNumber());
        systemInfo1.setUser(user);
        systemInfo1.setSocialMediaURL(systemInfo.getSocialMediaURL());

        systemInfoRepository.save(systemInfo1);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add system info success");
        apiResponse.setData(systemInfo1);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getSystemInfo(Long id) {
        APIResponse apiResponse = new APIResponse();

        SystemInfo systemInfo1 = systemInfoRepository.findById(id).orElseThrow(
                () -> new NotFoundException("System info not found !")
        );

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get system info success");
        apiResponse.setData(systemInfo1);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse updateSystemInfo(Long id, SystemInfo systemInfo, MultipartFile img) throws IOException {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findById(systemInfo.getUser().getId()).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        SystemInfo systemInfo1 = systemInfoRepository.findById(id).orElseThrow(
                () -> new NotFoundException("System info not found !")
        );

        if (systemInfo.getSystemName() != null) {
            systemInfo1.setSystemName(systemInfo.getSystemName());
        }

        if (systemInfo.getEmail() != null) {
            systemInfo1.setEmail(systemInfo.getEmail());
        }

        if (systemInfo.getAddress() != null) {
            systemInfo1.setAddress(systemInfo.getAddress());
        }

        if (img != null) {
            systemInfo1.setImg(imgService.updateImg(systemInfo1.getImg(), img));
        }

        systemInfo1.setDeleted(false); // giữ mặc định

        if (systemInfo.getSlogan() != null) {
            systemInfo1.setSlogan(systemInfo.getSlogan());
        }

        if (systemInfo.getDescription() != null) {
            systemInfo1.setDescription(systemInfo.getDescription());
        }

        if (systemInfo.getPhoneNumber() != null) {
            systemInfo1.setPhoneNumber(systemInfo.getPhoneNumber());
        }

        if (user != null) {
            systemInfo1.setUser(user);
        }

        if (systemInfo.getSocialMediaURL() != null) {
            systemInfo1.setSocialMediaURL(systemInfo.getSocialMediaURL());
        }


        systemInfoRepository.save(systemInfo1);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Update system info success");
        apiResponse.setData(systemInfo1);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse deleteSystemInfo(Long id) {
        APIResponse apiResponse = new APIResponse();

        SystemInfo systemInfo1 = systemInfoRepository.findById(id).orElseThrow(
                () -> new NotFoundException("System info not found !")
        );

        systemInfoRepository.delete(systemInfo1);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Delete system info success");
        apiResponse.setData(systemInfo1);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}
