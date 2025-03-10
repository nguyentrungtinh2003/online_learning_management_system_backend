package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestBlog;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Blog.BlogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addBlog(@Valid @RequestPart(value = "blog") APIRequestBlog apiRequestBlog,
                                               @RequestPart(value = "img",required = false) MultipartFile img,
                                               @RequestPart(value = "video",required = false) MultipartFile video) throws Exception {
        return ResponseEntity.ok(blogService.addBlog(apiRequestBlog, img, video));
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllBlog() {
        return ResponseEntity.ok(blogService.getAllBlog());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getBlogById(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.getBlogById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<APIResponse> getBlogByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(blogService.getBlogByUserId(userId));
    }


    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchBlog(@RequestParam String keyword,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) throws Exception {
        return ResponseEntity.ok(blogService.searchBlog(keyword, page,size));
    }

    @GetMapping("/page")
    public ResponseEntity<APIResponse> getBlogByPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size) throws Exception {
        return ResponseEntity.ok(blogService.getBlogByPage(page,size));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse> updateBlog(@PathVariable Long id, @Valid @RequestPart(value = "blog") APIRequestBlog apiRequestBlog,
                                               @RequestPart(value = "img",required = false) MultipartFile img,
                                               @RequestPart(value = "video",required = false) MultipartFile video) throws Exception {
        return ResponseEntity.ok(blogService.updateBlog(id, apiRequestBlog, img, video));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteBlog(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(blogService.deleteBlog(id));
    }
}
