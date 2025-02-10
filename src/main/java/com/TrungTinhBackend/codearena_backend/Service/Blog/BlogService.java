package com.TrungTinhBackend.codearena_backend.Service.Blog;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestBlog;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

public interface BlogService {
    public APIResponse addBlog(APIRequestBlog apiRequestBlog, MultipartFile img, MultipartFile video) throws Exception;
    public APIResponse updateBlog(Long id, APIRequestBlog apiRequestBlog, MultipartFile img, MultipartFile video) throws Exception;
    public APIResponse deleteBlog(Long id) throws Exception;
    public APIResponse searchBlog(String keyword, int page, int size);
}
