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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public APIResponse getAllLessonComment() {
        APIResponse apiResponse = new APIResponse();

        List<LessonComment> lessonComments = lessonCommentRepository.findAll();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get all lesson comment success !");
        apiResponse.setData(lessonComments);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getLessonCommentById(Long id) {
        APIResponse apiResponse = new APIResponse();

        LessonComment lessonComment = lessonCommentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Lesson comment not found !")
        );

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get lesson comment by id success !");
        apiResponse.setData(lessonComment);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse deleteLessonComment(Long id) {
        APIResponse apiResponse = new APIResponse();

        LessonComment lessonComment = lessonCommentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Lesson comment not found !")
        );

        lessonComment.setDeleted(true);
        lessonCommentRepository.save(lessonComment);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Delete lesson comment by id success !");
        apiResponse.setData(lessonComment);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse searchLessonComment(String keyword, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);

        Page<LessonComment> lessonComments = lessonCommentRepository.searchLessonComment(keyword,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search lesson comment success !");
        apiResponse.setData(lessonComments);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getLessonCommentByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);

        Page<LessonComment> lessonComments = lessonCommentRepository.findAll(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get lesson comment by page success !");
        apiResponse.setData(lessonComments);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}
