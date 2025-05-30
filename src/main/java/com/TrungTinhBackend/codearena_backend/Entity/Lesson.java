package com.TrungTinhBackend.codearena_backend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lessonName;

    private String description;

    private String img;

    private LocalDateTime date;
    private LocalDateTime updateDate;

    private String videoURL;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonBackReference
    private Course course;

    @OneToMany(mappedBy = "lesson")
    private List<Quiz> quizzes;

    @OneToMany(mappedBy = "lesson")
    @JsonIgnore()
    private List<LessonComment> lessonComments;
}
