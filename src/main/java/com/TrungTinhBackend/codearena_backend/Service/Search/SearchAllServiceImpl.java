package com.TrungTinhBackend.codearena_backend.Service.Search;

import com.TrungTinhBackend.codearena_backend.Entity.Blog;
import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Repository.BlogRepository;
import com.TrungTinhBackend.codearena_backend.Repository.CourseRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestSearchAllResult;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.BlogSpecification;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.CourseSpecification;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SearchAllServiceImpl implements SearchAllService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private BlogRepository blogRepository;

    public SearchAllServiceImpl(UserRepository userRepository, CourseRepository courseRepository, BlogRepository blogRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.blogRepository = blogRepository;
    }

    @Override
    public APIResponse searchAll(String keyword) {

        Specification<User> userSpecification = UserSpecification.searchByKeyword(keyword);
        Specification<Course> courseSpecification = CourseSpecification.searchByKeyword(keyword
        );
        Specification<Blog> blogSpecification = BlogSpecification.searchByKeyword(keyword);

        List<User> users = userRepository.findAll(userSpecification);
        List<Course> courses = courseRepository.findAll(courseSpecification);
        List<Blog> blogs = blogRepository.findAll(blogSpecification);

        APIRequestSearchAllResult apiRequestSearchAllResult = new APIRequestSearchAllResult();
        apiRequestSearchAllResult.setUsers(users);
        apiRequestSearchAllResult.setCourses(courses);
        apiRequestSearchAllResult.setBlogs(blogs);

        APIResponse apiResponse = new APIResponse();
        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search all success !");
        apiResponse.setData(apiRequestSearchAllResult);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}
