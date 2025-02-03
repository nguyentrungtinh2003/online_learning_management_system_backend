package com.TrungTinhBackend.codearena_backend.Request;

import com.TrungTinhBackend.codearena_backend.Entity.Blog;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class APIRequestBlogComment {

    private Long id;

    private String content;

    private String img;

    private String video;

    private User user;

    private Blog blog;
}
