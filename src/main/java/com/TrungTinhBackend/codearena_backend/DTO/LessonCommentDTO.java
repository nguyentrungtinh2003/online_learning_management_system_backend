package com.TrungTinhBackend.codearena_backend.DTO;

import com.TrungTinhBackend.codearena_backend.Entity.Lesson;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import lombok.Data;

@Data
public class LessonCommentDTO {

    private String content;

//    private String img;
//
//    private String video;

    private Long userId;

    private Long lessonId;
}
