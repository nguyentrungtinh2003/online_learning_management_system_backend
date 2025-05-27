package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.DTO.CourseMaterialDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.CourseMaterial.CourseMaterialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/course-materials")
public class CourseMaterialController {

    @Autowired
    private CourseMaterialService courseMaterialService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addCourseMaterial(@Valid @RequestPart(value = "course-material") CourseMaterialDTO courseMaterialDTO,
                                                 @RequestPart(value = "file",required = false) MultipartFile file) throws IOException {
        return ResponseEntity.ok(courseMaterialService.addCourseMaterial(courseMaterialDTO, file));
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllCourseMaterial() {
        return ResponseEntity.ok(courseMaterialService.getAllCourseMaterial());
    }

    @GetMapping("/page")
    public ResponseEntity<APIResponse> getCourseMaterialByPage(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(courseMaterialService.getCourseMaterialByPage(page,size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getCourseMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(courseMaterialService.getCourseMaterialById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchCourseMaterial(@RequestParam String keyword,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) throws Exception {
        return ResponseEntity.ok(courseMaterialService.searchCourseMaterial(keyword, page,size));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse> updateCourseMaterial(@PathVariable Long id, @Valid @RequestPart(value = "course-material") CourseMaterialDTO courseMaterialDTO,
                                                         @RequestPart(value = "file",required = false) MultipartFile file) throws IOException {
        return ResponseEntity.ok(courseMaterialService.updateCourseMaterial(id,courseMaterialDTO, file));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteCourseMaterial(@PathVariable Long id) {
        return ResponseEntity.ok(courseMaterialService.deleteCourseMaterial(id));
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<APIResponse> restoreCourseMaterial(@PathVariable Long id) {
        return ResponseEntity.ok(courseMaterialService.restoreCourseMaterial(id));
    }
}
