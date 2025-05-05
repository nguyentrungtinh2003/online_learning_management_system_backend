package com.TrungTinhBackend.codearena_backend.DTO;

import com.TrungTinhBackend.codearena_backend.Entity.Lesson;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import lombok.Data;

@Data
public class LessonCommentDTO {

    private Long id;

    private String content;

//    private String img;
//
//    private String video;

    private Long userId;

    private String username;

    private String img;

    private Long lessonId;
}
