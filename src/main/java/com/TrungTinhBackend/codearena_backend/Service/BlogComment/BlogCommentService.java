package com.TrungTinhBackend.codearena_backend.Service.BlogComment;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestBlogComment;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

public interface BlogCommentService {
    public APIResponse addBlogComment(APIRequestBlogComment apiRequestBlogComment, MultipartFile img, MultipartFile video) throws Exception;
    public APIResponse deleteBlogComment(Long id) throws Exception;
    public APIResponse searchBlogComment(String keyword, int page, int size);
    public APIResponse getBlogCommentByPage(int page, int size);
    public APIResponse getAllBlogComment();
    public APIResponse getBlogCommentById(Long id);
    public APIResponse getBlogCommentByBlogId(Long blogId);
}
