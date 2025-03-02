package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestBlog;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestBlogComment;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.BlogComment.BlogCommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/blog-comments")
public class BlogCommentController {

    @Autowired
    private BlogCommentService blogCommentService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addBlogComment(@Valid @RequestPart(value = "blogComment") APIRequestBlogComment apiRequestBlogComment,
                                               @RequestPart(value = "img",required = false) MultipartFile img,
                                               @RequestPart(value = "video",required = false) MultipartFile video) throws Exception {
        return ResponseEntity.ok(blogCommentService.addBlogComment(apiRequestBlogComment, img, video));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchBlogComment(@RequestParam String keyword,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) throws Exception {
        return ResponseEntity.ok(blogCommentService.searchBlogComment(keyword, page,size));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteBlogComment(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(blogCommentService.deleteBlogComment(id));
    }
}
