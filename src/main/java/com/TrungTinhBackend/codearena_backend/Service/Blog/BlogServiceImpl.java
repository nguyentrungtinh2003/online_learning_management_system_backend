package com.TrungTinhBackend.codearena_backend.Service.Blog;

import com.TrungTinhBackend.codearena_backend.DTO.UserPointHistoryDTO;
import com.TrungTinhBackend.codearena_backend.Entity.Blog;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.BlogRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.DTO.BlogDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.BlogSpecification;
import com.TrungTinhBackend.codearena_backend.Service.User.UserService;
import com.TrungTinhBackend.codearena_backend.Service.UserPointHistory.UserPointHistoryService;
import com.TrungTinhBackend.codearena_backend.Service.Video.VideoService;
import com.TrungTinhBackend.codearena_backend.Service.WebSocket.WebSocketSender;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private ImgService imgService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WebSocketSender webSocketSender;

    @Autowired
    private UserPointHistoryService userPointHistoryService;

    public BlogServiceImpl(BlogRepository blogRepository, ImgService imgService, VideoService videoService, UserRepository userRepository, UserService userService, WebSocketSender webSocketSender, UserPointHistoryService userPointHistoryService) {
        this.blogRepository = blogRepository;
        this.imgService = imgService;
        this.videoService = videoService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.webSocketSender = webSocketSender;
        this.userPointHistoryService = userPointHistoryService;
    }

    @Override
    public APIResponse addBlog(BlogDTO blogDTO, MultipartFile img, MultipartFile video) throws Exception {
       APIResponse apiResponse = new APIResponse();

        User user = userRepository.findById(blogDTO.getUserId()).orElseThrow(
                () -> new NotFoundException("User not found !")
        );
            Blog blog = new Blog();

            blog.setBlogName(blogDTO.getBlogName());
            blog.setDescription(blogDTO.getDescription());
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
            blog.setUser(user);

            blogRepository.save(blog);

        userPointHistoryService.addUserPointHistory(new UserPointHistoryDTO(user.getId(),5L));

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Add blog success !");
            apiResponse.setData(blog);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;



    }

    @Override
    public APIResponse updateBlog(Long id, BlogDTO blogDTO, MultipartFile img, MultipartFile video) throws Exception {
        APIResponse apiResponse = new APIResponse();

            Blog blog = blogRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Blog not found by id " + id)
            );

            blog.setBlogName(blogDTO.getBlogName());
            blog.setDescription(blogDTO.getDescription());
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
    }

    @Override
    public APIResponse deleteBlog(Long id,Long userId) {
        APIResponse apiResponse = new APIResponse();

            Blog blog = blogRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Blog not found by id " + id)
            );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found !")
        );

            blog.setDeleted(true);

            blogRepository.save(blog);

        userPointHistoryService.addUserPointHistory(new UserPointHistoryDTO(user.getId(),-5L));

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Delete blog success !");
            apiResponse.setData(blog);
            apiResponse.setTimestamp(LocalDateTime.now());

        webSocketSender.sendUserInfo(user);

            return apiResponse;
    }

    @Override
    public APIResponse searchBlog(String keyword, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Specification<Blog> specification = BlogSpecification.searchByKeyword(keyword);
        Page<Blog> blogs = blogRepository.findAll(specification,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search blog success !");
        apiResponse.setData(blogs);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getBlogByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<Blog> blogs = blogRepository.findAll(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get blog by page success !");
        apiResponse.setData(blogs);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getAllBlog() {
        APIResponse apiResponse = new APIResponse();

        List<Blog> blogs = blogRepository.findAll();

        List<BlogDTO> blogDTOs = blogs.stream()
                .map(
                        blog -> {
                            BlogDTO blogDTO = new BlogDTO();
                            blogDTO.setDeleted(blog.isDeleted());
                            blogDTO.setBlogName(blog.getBlogName());
                            blogDTO.setId(blog.getId());
                            blogDTO.setUserId(blog.getUser().getId());
                            blogDTO.setUsername(blog.getUser().getUsername());
                            blogDTO.setDescription(blog.getDescription());
                            blogDTO.setImg(blog.getImg());
                            blogDTO.setVideo(blog.getVideo());
                            blogDTO.setLikedUsers(blog.getLikedUsers().stream().map(User::getId).collect(Collectors.toSet()));
                            return blogDTO;
                        }
                )
                .toList();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get all blog success !");
        apiResponse.setData(blogDTOs);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getBlogById(Long id) {
        APIResponse apiResponse = new APIResponse();

        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Blog not found !")
        );

        blog.setView(blog.getView() + 1);
        blogRepository.save(blog);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get blog by id success !");
        apiResponse.setData(blog);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getBlogByUserId(Long userId) {
        APIResponse apiResponse = new APIResponse();

        List<Blog> blogs = blogRepository.findByUserId(userId);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get blog by userId success !");
        apiResponse.setData(blogs);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    @Transactional
    public APIResponse likeBlog(Long blogId, Long userId) {
        APIResponse apiResponse = new APIResponse();
        try {
            Blog blog = blogRepository.findById(blogId)
                    .orElseThrow(() -> new NotFoundException("Blog not found !"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found !"));

            if (!blog.getLikedUsers().contains(user)) {
                if (blog.getLikeCount() == null) blog.setLikeCount(0L);
                blog.setLikeCount(blog.getLikeCount() + 1);
                blog.getLikedUsers().add(user);

                blogRepository.save(blog);
            }

            List<Long> likedUserIds = blog.getLikedUsers()
                    .stream()
                    .map(User::getId)
                    .collect(Collectors.toList());


            userPointHistoryService.addUserPointHistory(new UserPointHistoryDTO(user.getId(),1L));

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("User like blog success !");
            apiResponse.setData(likedUserIds);
            apiResponse.setTimestamp(LocalDateTime.now());

            webSocketSender.sendLike(blogId,userId,likedUserIds);

            return apiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            apiResponse.setStatusCode(500L);
            apiResponse.setMessage("Error: " + e.getMessage());
            return apiResponse;
        }
    }

    @Override
    @Transactional
    public APIResponse unLikeBlog(Long blogId, Long userId) {
        APIResponse apiResponse = new APIResponse();
        try {
            Blog blog = blogRepository.findById(blogId)
                    .orElseThrow(() -> new NotFoundException("Blog not found !"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found !"));

            if (blog.getLikedUsers().contains(user)) {
                if (blog.getLikeCount() == null) blog.setLikeCount(0L);
                if (blog.getLikeCount() > 0) blog.setLikeCount(blog.getLikeCount() - 1);
                blog.getLikedUsers().remove(user);

                blogRepository.save(blog);
            }

            List<Long> likedUserIds = blog.getLikedUsers()
                    .stream()
                    .map(User::getId)
                    .collect(Collectors.toList());

//            user.setPoint(user.getPoint() - 1);
            userPointHistoryService.addUserPointHistory(new UserPointHistoryDTO(user.getId(),-1L));

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("User un like blog success !");
            apiResponse.setData(likedUserIds);
            apiResponse.setTimestamp(LocalDateTime.now());

            webSocketSender.sendUnLike(blogId,userId,likedUserIds);

            return apiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            apiResponse.setStatusCode(500L);
            apiResponse.setMessage("Error: " + e.getMessage());
            return apiResponse;
        }
    }

    @Override
    public APIResponse getBlogsLikedByUser(Long userId) {
        APIResponse apiResponse = new APIResponse();

        List<Blog> blogs = blogRepository.findBlogsLikedByUser(userId);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get blogs liked by user id success !");
        apiResponse.setData(blogs);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse restoreBlog(Long id, Long userId) {
        APIResponse apiResponse = new APIResponse();

        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Blog not found by id " + id)
        );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found by id " + userId)
        );

        blog.setDeleted(false);

        blogRepository.save(blog);

        user.setPoint(user.getPoint() + 5);
        user.setRankEnum(userService.calculateRank(user.getPoint()));
        userRepository.save(user);
        webSocketSender.sendUserInfo(user);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Restore blog success !");
        apiResponse.setData(blog);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getRandomBlog(int limit) {
        APIResponse apiResponse = new APIResponse();

        List<Blog> blogs = blogRepository.findRandomBlogs(limit);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get blogs random success !");
        apiResponse.setData(blogs);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

}
