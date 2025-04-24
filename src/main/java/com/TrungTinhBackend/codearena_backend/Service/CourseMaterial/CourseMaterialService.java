package com.TrungTinhBackend.codearena_backend.Service.CourseMaterial;

import com.TrungTinhBackend.codearena_backend.DTO.CourseMaterialDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CourseMaterialService {
    public APIResponse addCourseMaterial(CourseMaterialDTO courseMaterialDTO, MultipartFile file) throws IOException;
    public APIResponse getAllCourseMaterial();
    public APIResponse getCourseMaterialByPage(int page, int size);
    public APIResponse updateCourseMaterial(Long id, CourseMaterialDTO courseMaterialDTO, MultipartFile file) throws IOException;
    public APIResponse deleteCourseMaterial(Long id);
    public APIResponse searchCourseMaterial(String keyword, int page, int size);
    public APIResponse getCourseMaterialById(Long id);
}
