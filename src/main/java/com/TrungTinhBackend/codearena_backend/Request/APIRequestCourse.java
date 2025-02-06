package com.TrungTinhBackend.codearena_backend.Request;

import com.TrungTinhBackend.codearena_backend.Entity.Lesson;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Enum.CourseEnum;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class APIRequestCourse {

    private Long id;

    private String courseName;

    private String description;

    private String img;

    private Double price;

    private CourseEnum courseEnum;

    private LocalDateTime date;

    private boolean isDeleted;

    private List<User> users;

    private User user;

    private List<Lesson> lessons;
}
