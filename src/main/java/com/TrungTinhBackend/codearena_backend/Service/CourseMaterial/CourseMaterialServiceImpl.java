package com.TrungTinhBackend.codearena_backend.Service.CourseMaterial;

import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.CourseMaterial;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.CourseMaterialRepository;
import com.TrungTinhBackend.codearena_backend.Repository.CourseRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.DTO.CourseMaterialDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.File.FileService;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.CourseMaterialSpecification;
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
import java.util.stream.Collectors;

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

    public CourseMaterialServiceImpl(CourseMaterialRepository courseMaterialRepository, UserRepository userRepository, CourseRepository courseRepository, FileService fileService) {
        this.courseMaterialRepository = courseMaterialRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.fileService = fileService;
    }

    @Override
    public APIResponse addCourseMaterial(CourseMaterialDTO courseMaterialDTO, MultipartFile file) throws IOException {
        APIResponse apiResponse = new APIResponse();

        CourseMaterial courseMaterial = new CourseMaterial();
        courseMaterial.setTitle(courseMaterialDTO.getTitle());
        courseMaterial.setDescription(courseMaterialDTO.getDescription());
        courseMaterial.setDeleted(false);
        courseMaterial.setUploadDate(LocalDateTime.now());
        if(file != null || file.isEmpty()) {
            courseMaterial.setFile(fileService.uploadFile(file));
        }

        courseMaterial.setCourse(courseRepository.findById(courseMaterialDTO.getCourseId()).orElseThrow(
                () -> new NotFoundException("Course not found !")
        ));

        courseMaterial.setLecturer(userRepository.findById(courseMaterialDTO.getLecturerId()).orElseThrow(
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

        List<CourseMaterialDTO> courseMaterialDTOS = courseMaterials.stream().map((courseMaterial -> {
            CourseMaterialDTO courseMaterialDTO = new CourseMaterialDTO();

            courseMaterialDTO.setId(courseMaterial.getId());
            courseMaterialDTO.setTitle(courseMaterial.getTitle());
            courseMaterialDTO.setCourseId(courseMaterial.getCourse().getId());
            courseMaterialDTO.setFile(courseMaterial.getFile());
            courseMaterialDTO.setCourseName(courseMaterial.getCourse().getCourseName());
            courseMaterialDTO.setDescription(courseMaterial.getDescription());
            courseMaterialDTO.setLecturerId(courseMaterial.getLecturer().getId());
            courseMaterialDTO.setLecturerName(courseMaterial.getLecturer().getUsername());
            courseMaterialDTO.setDeleted(courseMaterial.isDeleted());

            return courseMaterialDTO;
        })).toList();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get all course material success !");
        apiResponse.setData(courseMaterialDTOS);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getCourseMaterialByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<CourseMaterial> courseMaterials = courseMaterialRepository.findAll(pageable);

        Page<CourseMaterialDTO> courseMaterialDTOS = courseMaterials.map((courseMaterial -> {
            CourseMaterialDTO courseMaterialDTO = new CourseMaterialDTO();

            courseMaterialDTO.setId(courseMaterial.getId());
            courseMaterialDTO.setTitle(courseMaterial.getTitle());
            courseMaterialDTO.setCourseId(courseMaterial.getCourse().getId());
            courseMaterialDTO.setFile(courseMaterial.getFile());
            courseMaterialDTO.setCourseName(courseMaterial.getCourse().getCourseName());
            courseMaterialDTO.setDescription(courseMaterial.getDescription());
            courseMaterialDTO.setLecturerId(courseMaterial.getLecturer().getId());
            courseMaterialDTO.setLecturerName(courseMaterial.getLecturer().getUsername());
            courseMaterialDTO.setDeleted(courseMaterial.isDeleted());

            return courseMaterialDTO;
        }));

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get course material by page success !");
        apiResponse.setData(courseMaterialDTOS);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse updateCourseMaterial(Long id, CourseMaterialDTO courseMaterialDTO, MultipartFile file) throws IOException {
        APIResponse apiResponse = new APIResponse();

        CourseMaterial courseMaterial = courseMaterialRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Course material not found !")
        );

        courseMaterial.setTitle(courseMaterialDTO.getTitle());
        courseMaterial.setDescription(courseMaterialDTO.getDescription());
        courseMaterial.setDeleted(false);
        courseMaterial.setUploadDate(LocalDateTime.now());

        if (file != null && !file.isEmpty()) {
            courseMaterial.setFile(fileService.uploadFile(file));
        }

        courseMaterial.setCourse(courseRepository.findById(courseMaterialDTO.getCourseId()).orElseThrow(
                () -> new NotFoundException("Course not found !")
        ));

        courseMaterial.setLecturer(userRepository.findById(courseMaterialDTO.getLecturerId()).orElseThrow(
                () -> new NotFoundException("Lecturer not found !")
        ));

        courseMaterialRepository.save(courseMaterial);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Update course material success !");
        apiResponse.setData(null);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse deleteCourseMaterial(Long id) {
        APIResponse apiResponse = new APIResponse();

        CourseMaterial courseMaterial = courseMaterialRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Course material not found !")
        );

        courseMaterial.setDeleted(true);
        courseMaterialRepository.save(courseMaterial);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Delete course material success !");
        apiResponse.setData(courseMaterial);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse restoreCourseMaterial(Long id) {
        APIResponse apiResponse = new APIResponse();

        CourseMaterial courseMaterial = courseMaterialRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Course material not found !")
        );

        courseMaterial.setDeleted(false);
        courseMaterialRepository.save(courseMaterial);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Restore course material success !");
        apiResponse.setData(courseMaterial);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse searchCourseMaterial(String keyword, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Specification<CourseMaterial> specification = CourseMaterialSpecification.searchByKeyword(keyword);
        Page<CourseMaterial> courseMaterials = courseMaterialRepository.findAll(specification,pageable);

        Page<CourseMaterialDTO> courseMaterialDTOS = courseMaterials.map((courseMaterial -> {
            CourseMaterialDTO courseMaterialDTO = new CourseMaterialDTO();

            courseMaterialDTO.setId(courseMaterial.getId());
            courseMaterialDTO.setTitle(courseMaterial.getTitle());
            courseMaterialDTO.setCourseId(courseMaterial.getCourse().getId());
            courseMaterialDTO.setFile(courseMaterial.getFile());
            courseMaterialDTO.setCourseName(courseMaterial.getCourse().getCourseName());
            courseMaterialDTO.setDescription(courseMaterial.getDescription());
            courseMaterialDTO.setLecturerId(courseMaterial.getLecturer().getId());
            courseMaterialDTO.setLecturerName(courseMaterial.getLecturer().getUsername());
            courseMaterialDTO.setDeleted(courseMaterial.isDeleted());

            return courseMaterialDTO;
        }));

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search course material success !");
        apiResponse.setData(courseMaterialDTOS);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getCourseMaterialById(Long id) {
        APIResponse apiResponse = new APIResponse();

        CourseMaterial courseMaterial = courseMaterialRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Course material not found !")
        );

        CourseMaterialDTO courseMaterialDTO = new CourseMaterialDTO();

        courseMaterialDTO.setId(courseMaterial.getId());
        courseMaterialDTO.setTitle(courseMaterial.getTitle());
        courseMaterialDTO.setCourseId(courseMaterial.getCourse().getId());
        courseMaterialDTO.setFile(courseMaterial.getFile());
        courseMaterialDTO.setCourseName(courseMaterial.getCourse().getCourseName());
        courseMaterialDTO.setDescription(courseMaterial.getDescription());
        courseMaterialDTO.setLecturerId(courseMaterial.getLecturer().getId());
        courseMaterialDTO.setLecturerName(courseMaterial.getLecturer().getUsername());
        courseMaterialDTO.setDeleted(courseMaterial.isDeleted());

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get course material by id success !");
        apiResponse.setData(courseMaterialDTO);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}
