package com.TrungTinhBackend.codearena_backend.Service.LessonComment;

import com.TrungTinhBackend.codearena_backend.Entity.*;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.LessonCommentRepository;
import com.TrungTinhBackend.codearena_backend.Repository.LessonRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestLessonComment;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Video.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class LessonCommentServiceImpl implements LessonCommentService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ImgService imgService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private LessonCommentRepository lessonCommentRepository;


    @Override
    public APIResponse addLessonComment(APIRequestLessonComment apiRequestLessonComment, MultipartFile img, MultipartFile video) throws IOException {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findById(apiRequestLessonComment.getUser().getId()).orElseThrow(
                () -> new NotFoundException("User not found !")
        );

        Lesson lesson = lessonRepository.findById(apiRequestLessonComment.getLesson().getId()).orElseThrow(
                () -> new NotFoundException("Lesson not found !")
        );

        LessonComment lessonComment = new LessonComment();
        lessonComment.setContent(apiRequestLessonComment.getContent());
        lessonComment.setLesson(lesson);
        lessonComment.setUser(user);
        if(img != null) {
            lessonComment.setImg(imgService.uploadImg(img));
        }
        if(video != null) {
            lessonComment.setVideo(videoService.uploadVideo(video));
        }
        lessonComment.setDate(LocalDateTime.now());
        lessonComment.setDeleted(false);

        lessonCommentRepository.save(lessonComment);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add lesson comment success !");
        apiResponse.setData(lessonComment);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}
