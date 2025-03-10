package com.TrungTinhBackend.codearena_backend.Service.CourseMaterial;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestCourseMaterial;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CourseMaterialService {
    public APIResponse addCourseMaterial(APIRequestCourseMaterial apiRequestCourseMaterial, MultipartFile file) throws IOException;
    public APIResponse getAllCourseMaterial();
    public APIResponse getCourseMaterialByPage(int page, int size);
    public APIResponse updateCourseMaterial(Long id,APIRequestCourseMaterial apiRequestCourseMaterial,MultipartFile file);
    public APIResponse deleteCourseMaterial(Long id);
}
