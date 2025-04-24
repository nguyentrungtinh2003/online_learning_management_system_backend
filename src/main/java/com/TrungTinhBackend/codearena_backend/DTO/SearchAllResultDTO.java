package com.TrungTinhBackend.codearena_backend.DTO;

import com.TrungTinhBackend.codearena_backend.Entity.Blog;
import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import lombok.Data;

import java.util.List;

@Data
public class SearchAllResultDTO {

    private List<Course> courses;

    private List<Blog> blogs;

    private List<User> users;
}
