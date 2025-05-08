package com.TrungTinhBackend.codearena_backend.Service.LessonComment;

import com.TrungTinhBackend.codearena_backend.DTO.BlogCommentDTO;
import com.TrungTinhBackend.codearena_backend.Entity.*;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.LessonCommentRepository;
import com.TrungTinhBackend.codearena_backend.Repository.LessonRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.DTO.LessonCommentDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Video.VideoService;
import com.TrungTinhBackend.codearena_backend.Service.WebSocket.WebSocketSender;
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

    @Autowired
    private WebSocketSender webSocketSender;

    public LessonCommentServiceImpl(UserRepository userRepository, LessonRepository lessonRepository, ImgService imgService, VideoService videoService, WebSocketSender webSocketSender) {
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.imgService = imgService;
        this.videoService = videoService;
        this.webSocketSender = webSocketSender;
    }


    @Override
    public APIResponse addLessonComment(LessonCommentDTO lessonCommentDTO) throws IOException {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findById(lessonCommentDTO.getUserId()).orElseThrow(
                () -> new NotFoundException("User not found !")
        );

        Lesson lesson = lessonRepository.findById(lessonCommentDTO.getLessonId()).orElseThrow(
                () -> new NotFoundException("Lesson not found !")
        );

        LessonComment lessonComment = new LessonComment();
        lessonComment.setContent(lessonCommentDTO.getContent());
        lessonComment.setLesson(lesson);
        lessonComment.setUser(user);
//        if(img != null) {
//            lessonComment.setImg(imgService.uploadImg(img));
//        }
//        if(video != null) {
//            lessonComment.setVideo(videoService.uploadVideo(video));
//        }
        lessonComment.setDate(LocalDateTime.now());
        lessonComment.setDeleted(false);

        user.setPoint(user.getPoint() + 2);
        userRepository.save(user);

        lessonCommentRepository.save(lessonComment);


        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add lesson comment success !");
        apiResponse.setData(lessonComment);
        apiResponse.setTimestamp(LocalDateTime.now());

        webSocketSender.sendLessonComment(lessonCommentDTO);
        webSocketSender.sendUserInfo(user);

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
    public APIResponse getLessonCommentByLessonId(Long lessonId) {
        APIResponse apiResponse = new APIResponse();

        List<LessonComment> lessonComments = lessonCommentRepository.findByLessonId(lessonId);

        List<LessonCommentDTO> lessonCommentDTOS = lessonComments.stream().map(lessonComment -> {
            LessonCommentDTO lessonCommentDTO = new LessonCommentDTO();
            lessonCommentDTO.setId(lessonComment.getId());
            lessonCommentDTO.setLessonId(lessonComment.getLesson().getId());
            lessonCommentDTO.setUserId(lessonComment.getUser().getId());
            lessonCommentDTO.setUsername(lessonComment.getUser().getUsername());
            lessonCommentDTO.setImg(lessonComment.getUser().getImg());
            lessonCommentDTO.setContent(lessonComment.getContent());
            return lessonCommentDTO;
        }).toList();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get lesson comment by lesson id success !");
        apiResponse.setData(lessonCommentDTOS);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse deleteLessonComment(Long id, Long userId) {
        APIResponse apiResponse = new APIResponse();

        LessonComment lessonComment = lessonCommentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Lesson comment not found !")
        );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found !")
        );

//        lessonComment.setDeleted(true);
//        lessonCommentRepository.save(lessonComment);

        lessonCommentRepository.delete(lessonComment);

        user.setPoint(user.getPoint() - 2);
        userRepository.save(user);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Delete lesson comment by id success !");
        apiResponse.setData(lessonComment);
        apiResponse.setTimestamp(LocalDateTime.now());

        webSocketSender.sendUserInfo(user);


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
