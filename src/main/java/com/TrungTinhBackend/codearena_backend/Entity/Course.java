package com.TrungTinhBackend.codearena_backend.Entity;

import com.TrungTinhBackend.codearena_backend.Enum.CourseEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;

    private String description;

    private String img;

    private Double price;

    private CourseEnum courseEnum;

    private LocalDateTime date;

    private boolean isDeleted;
    
    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "course")
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "course")
    private List<CourseMaterial> courseMaterials;
}
