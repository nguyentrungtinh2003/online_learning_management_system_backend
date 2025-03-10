package com.TrungTinhBackend.codearena_backend.Service.CourseMaterial;

import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.CourseMaterial;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.CourseMaterialRepository;
import com.TrungTinhBackend.codearena_backend.Repository.CourseRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestCourseMaterial;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.File.FileService;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.CourseSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseMaterialServiceImpl implements CourseMaterialService{

    @Autowired
    private CourseMaterialRepository courseMaterialRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private FileService fileService;

    @Override
    public APIResponse addCourseMaterial(APIRequestCourseMaterial apiRequestCourseMaterial, MultipartFile file) throws IOException {
        APIResponse apiResponse = new APIResponse();

        CourseMaterial courseMaterial = new CourseMaterial();
        courseMaterial.setTitle(apiRequestCourseMaterial.getTitle());
        courseMaterial.setDescription(apiRequestCourseMaterial.getDescription());
        courseMaterial.setDeleted(false);
        courseMaterial.setUploadDate(LocalDateTime.now());
        if(file != null || file.isEmpty()) {
            courseMaterial.setFile(fileService.uploadFile(file));
        }

        courseMaterial.setCourse(courseRepository.findById(apiRequestCourseMaterial.getCourse().getId()).orElseThrow(
                () -> new NotFoundException("Course not found !")
        ));

        courseMaterial.setLecturer(userRepository.findById(apiRequestCourseMaterial.getLecturer().getId()).orElseThrow(
                () -> new NotFoundException("Lecturer not found !")
        ));

        courseMaterialRepository.save(courseMaterial);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add course material success !");
        apiResponse.setData(courseMaterial);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getAllCourseMaterial() {
        APIResponse apiResponse = new APIResponse();

        List<CourseMaterial> courseMaterials = courseMaterialRepository.findAll();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get all course material success !");
        apiResponse.setData(courseMaterials);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getCourseMaterialByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<CourseMaterial> courseMaterials = courseMaterialRepository.findAll(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get course material by page success !");
        apiResponse.setData(courseMaterials);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse updateCourseMaterial(Long id, APIRequestCourseMaterial apiRequestCourseMaterial, MultipartFile file) {
        return null;
    }

    @Override
    public APIResponse deleteCourseMaterial(Long id) {
        return null;
    }
}
