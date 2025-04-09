package com.TrungTinhBackend.codearena_backend.Entity;

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

    private String videoURL;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnore()
    private Course course;

    @OneToMany(mappedBy = "lesson")
    private List<Quiz> quizzes;

    @OneToOne(mappedBy = "lesson", cascade = CascadeType.ALL)
    private ChatRoom chatRoom;

    @OneToMany(mappedBy = "lesson")
    private List<LessonComment> lessonComments;
}
