package com.TrungTinhBackend.codearena_backend.Request;

import com.TrungTinhBackend.codearena_backend.Entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class APIRequestBlog {

    private Long id;

    private String blogName;

    private String description;

    private String img;

    private String video;


}
