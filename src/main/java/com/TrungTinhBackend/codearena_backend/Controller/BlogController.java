package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.DTO.BlogDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.AuditLog.AuditLogService;
import com.TrungTinhBackend.codearena_backend.Service.Blog.BlogService;
import com.TrungTinhBackend.codearena_backend.Utils.SecurityUtils;
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

    @Autowired
    private AuditLogService auditLogService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addBlog(@Valid @RequestPart(value = "blog") BlogDTO blogDTO,
                                               @RequestPart(value = "img",required = false) MultipartFile img,
                                               @RequestPart(value = "video",required = false) MultipartFile video) throws Exception {
        String username = SecurityUtils.getCurrentUsername();
        auditLogService.addLog(username,"ADD","Add blog");
        return ResponseEntity.ok(blogService.addBlog(blogDTO, img, video));
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllBlog() {
        return ResponseEntity.ok(blogService.getAllBlog());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getBlogById(@PathVariable Long id) {
        String username = SecurityUtils.getCurrentUsername();
        auditLogService.addLog(username,"VIEW","View blog "+id);
        return ResponseEntity.ok(blogService.getBlogById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<APIResponse> getBlogByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(blogService.getBlogByUserId(userId));
    }

    @PostMapping("/like/{blogId}/{userId}")
    public ResponseEntity<APIResponse> likeBlog(@PathVariable Long blogId,
                                                @PathVariable Long userId) {
        return ResponseEntity.ok(blogService.likeBlog(blogId,userId));
    }

    @PostMapping("/unlike/{blogId}/{userId}")
    public ResponseEntity<APIResponse> unLikeBlog(@PathVariable Long blogId,
                                                @PathVariable Long userId) {
        return ResponseEntity.ok(blogService.unLikeBlog(blogId,userId));
    }

    @GetMapping("/userLiked/{userId}")
    public ResponseEntity<APIResponse> getBlogLikedByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(blogService.getBlogsLikedByUser(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchBlog(@RequestParam String keyword,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) throws Exception {
        String username = SecurityUtils.getCurrentUsername();
        auditLogService.addLog(username,"SEARCH","Search blog keyword "+keyword);
        return ResponseEntity.ok(blogService.searchBlog(keyword, page,size));
    }

    @GetMapping("/page")
    public ResponseEntity<APIResponse> getBlogByPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size) throws Exception {
        return ResponseEntity.ok(blogService.getBlogByPage(page,size));
    }

    @GetMapping("/limit")
    public ResponseEntity<APIResponse> getBlogRandom(@RequestParam(defaultValue = "10") int limit) throws Exception {
        return ResponseEntity.ok(blogService.getRandomBlog(limit));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse> updateBlog(@PathVariable Long id, @Valid @RequestPart(value = "blog") BlogDTO blogDTO,
                                               @RequestPart(value = "img",required = false) MultipartFile img,
                                               @RequestPart(value = "video",required = false) MultipartFile video) throws Exception {
        String username = SecurityUtils.getCurrentUsername();
        auditLogService.addLog(username,"UPDATE","Update blog");
        return ResponseEntity.ok(blogService.updateBlog(id, blogDTO, img, video));
    }

    @DeleteMapping("/delete/{id}/{userId}")
    public ResponseEntity<APIResponse> deleteBlog(@PathVariable Long id,
                                                  @PathVariable Long userId) throws Exception {
        String username = SecurityUtils.getCurrentUsername();
        auditLogService.addLog(username,"DELETE","Delete blog "+id);
        return ResponseEntity.ok(blogService.deleteBlog(id,userId));
    }

    @PutMapping("/restore/{id}/{userId}")
    public ResponseEntity<APIResponse> restoreBlog(@PathVariable Long id,
                                                   @PathVariable Long userId) throws Exception {
        String username = SecurityUtils.getCurrentUsername();
        auditLogService.addLog(username,"RESTORE","Restore blog "+id);
        return ResponseEntity.ok(blogService.restoreBlog(id, userId));
    }
}
