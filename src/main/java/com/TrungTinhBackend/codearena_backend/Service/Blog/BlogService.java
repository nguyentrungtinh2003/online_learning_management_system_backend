package com.TrungTinhBackend.codearena_backend.Service.Blog;

import com.TrungTinhBackend.codearena_backend.DTO.BlogDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

public interface BlogService {
    public APIResponse addBlog(BlogDTO blogDTO, MultipartFile img, MultipartFile video) throws Exception;
    public APIResponse updateBlog(Long id, BlogDTO blogDTO, MultipartFile img, MultipartFile video) throws Exception;
    public APIResponse deleteBlog(Long id) throws Exception;
    public APIResponse searchBlog(String keyword, int page, int size);
    public APIResponse getBlogByPage(int page, int size);
    public APIResponse getAllBlog();
    public APIResponse getBlogById(Long id);
    public APIResponse getBlogByUserId(Long userId);
    public APIResponse likeBlog(Long userId, Long blogId);
    public APIResponse unLikeBlog(Long userId, Long blogId);
    public APIResponse getBlogsLikedByUser(Long userId);
}
