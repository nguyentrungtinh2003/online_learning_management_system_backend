package com.TrungTinhBackend.codearena_backend.Service.Search;

import com.TrungTinhBackend.codearena_backend.Entity.Blog;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Repository.BlogRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestSearchAllResult;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SearchAllServiceImpl implements SearchAllService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogRepository blogRepository;


    @Override
    public APIResponse searchAll(String keyword) {

        var userPage = userRepository.searchUser(keyword, PageRequest.of(0, 5));
        List<User> users = (userPage != null) ? userPage.getContent() : List.of();

        var blogPage = blogRepository.searchBlog(keyword, PageRequest.of(0, 5));
        List<Blog> blogs = (blogPage != null) ? blogPage.getContent() : List.of();


        APIRequestSearchAllResult apiRequestSearchAllResult = new APIRequestSearchAllResult();
        apiRequestSearchAllResult.setBlogs(blogs);
        apiRequestSearchAllResult.setUsers(users);

        APIResponse apiResponse = new APIResponse();
        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search all success !");
        apiResponse.setData(apiRequestSearchAllResult);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}
