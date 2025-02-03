package com.TrungTinhBackend.codearena_backend.Service.Blog;

import com.TrungTinhBackend.codearena_backend.Entity.Blog;
import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Repository.BlogRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestBlog;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Video.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private ImgService imgService;

    @Autowired
    private VideoService videoService;

    @Override
    public APIResponse addBlog(APIRequestBlog apiRequestBlog, MultipartFile img, MultipartFile video) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {
            Blog blog = new Blog();

            blog.setBlogName(apiRequestBlog.getBlogName());
            blog.setDescription(apiRequestBlog.getDescription());
            if(img != null && !img.isEmpty()) {
                blog.setImg(imgService.uploadImg(img));
            }
            if(video != null && !video.isEmpty()) {
                blog.setVideo(videoService.uploadVideo(video));
            }
            blog.setDate(LocalDateTime.now());
            blog.setDeleted(false);
            blog.setLikeCount(0L);
            blog.setView(0L);

            blogRepository.save(blog);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Add blog success !");
            apiResponse.setData(blog);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }

    @Override
    public APIResponse updateBlog(Long id, APIRequestBlog apiRequestBlog, MultipartFile img, MultipartFile video) throws Exception {
        APIResponse apiResponse = new APIResponse();
        try {
            Blog blog = blogRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("Blog not found !")
            );

            blog.setBlogName(apiRequestBlog.getBlogName());
            blog.setDescription(apiRequestBlog.getDescription());
            if(img != null && !img.isEmpty()) {
                blog.setImg(imgService.updateImg(blog.getImg(),img));
            }
            if(video != null && !video.isEmpty()) {
                blog.setVideo(videoService.updateVideo(blog.getVideo(), video));
            }

            blogRepository.save(blog);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Update blog success !");
            apiResponse.setData(blog);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new Exception("Message : "+e.getMessage(),e);
        }
    }
}
