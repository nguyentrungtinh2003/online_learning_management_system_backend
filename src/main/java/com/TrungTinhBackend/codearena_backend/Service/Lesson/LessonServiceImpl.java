package com.TrungTinhBackend.codearena_backend.Service.Lesson;

import com.TrungTinhBackend.codearena_backend.Entity.*;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.ChatRoomRepository;
import com.TrungTinhBackend.codearena_backend.Repository.CourseRepository;
import com.TrungTinhBackend.codearena_backend.Repository.LessonRepository;
import com.TrungTinhBackend.codearena_backend.Repository.QuizRepository;
import com.TrungTinhBackend.codearena_backend.DTO.LessonDTO;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.LessonSpecification;
import com.TrungTinhBackend.codearena_backend.Service.Video.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LessonServiceImpl implements LessonService{

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ImgService imgService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public LessonServiceImpl(LessonRepository lessonRepository, ImgService imgService, VideoService videoService, CourseRepository courseRepository, QuizRepository quizRepository, ChatRoomRepository chatRoomRepository) {
        this.lessonRepository = lessonRepository;
        this.imgService = imgService;
        this.videoService = videoService;
        this.courseRepository = courseRepository;
        this.quizRepository = quizRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public APIResponse addLesson(LessonDTO lessonDTO, MultipartFile img, MultipartFile video) throws Exception {
        APIResponse apiResponse = new APIResponse();

            Course course = courseRepository.findById(lessonDTO.getCourseId()).orElseThrow(
                    () -> new NotFoundException("Course not found by id " + lessonDTO.getCourseId())
            );

            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setChatRoomName("Phòng chat " + lessonDTO.getLessonName());
            chatRoom.setDeleted(false);

            chatRoomRepository.save(chatRoom);

            Lesson lesson = new Lesson();

            lesson.setLessonName(lessonDTO.getLessonName());
            lesson.setDescription(lessonDTO.getDescription());
            lesson.setDate(LocalDateTime.now());
            lesson.setImg(imgService.uploadImg(img));
            lesson.setVideoURL(videoService.uploadVideo(video));
            lesson.setDeleted(false);
            lesson.setCourse(course);
            lesson.setQuizzes(null);
            lesson.setChatRoom(chatRoom);

            lessonRepository.save(lesson);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Add lesson success !");
            apiResponse.setData(lesson);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
    }

    @Override
    public APIResponse getAllLesson() {
        APIResponse apiResponse = new APIResponse();

        List<Lesson> lessons = lessonRepository.findByIsDeletedFalse();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get all lesson success !");
        apiResponse.setData(lessons);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getLessonById(Long id) {
        APIResponse apiResponse = new APIResponse();

        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Lesson not found !")
        );

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get lesson by id success !");
        apiResponse.setData(lesson);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse updateLesson(Long id, LessonDTO lessonDTO, MultipartFile img, MultipartFile video) throws Exception {
        APIResponse apiResponse = new APIResponse();

            Course course = courseRepository.findById(lessonDTO.getCourseId()).orElseThrow(
                    () -> new NotFoundException("Course not found by id " + lessonDTO.getCourseId())
            );

            Lesson lesson = lessonRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Lesson not found by id " + id)
            );

            if(lessonDTO.getLessonName() != null && !lessonDTO.getLessonName().isEmpty()) {
                lesson.setLessonName(lessonDTO.getLessonName());
            }
            if(lessonDTO.getDescription() != null && !lessonDTO.getDescription().isEmpty()) {
                lesson.setDescription(lessonDTO.getDescription());
            }
            if(img != null && !img.isEmpty()) {
                lesson.setImg(imgService.updateImg(lesson.getImg(),img));
            }
            if(video != null && !video.isEmpty()) {
                lesson.setVideoURL(videoService.updateVideo(lesson.getVideoURL(),video));
            }
            if(lessonDTO.getCourseId() != null) {
                lesson.setCourse(course);
            }
            if (lessonDTO.getQuizzes() != null && !lessonDTO.getQuizzes().isEmpty()) {
                List<Quiz> quizList = quizRepository.findAllById(
                        lessonDTO.getQuizzes().stream().map(Quiz::getId).toList()
                );

                if (quizList.size() != lessonDTO.getQuizzes().size()) {
                    throw new NotFoundException("One or more quizzes not found !");
                }

                lesson.setQuizzes(quizList);
            }

            lessonRepository.save(lesson);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Update lesson success !");
            apiResponse.setData(lesson);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
    }

    @Override
    public APIResponse deleteLesson(Long id) throws Exception {
        APIResponse apiResponse = new APIResponse();

            Lesson lesson = lessonRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Lesson not found by id " + id)
            );

            lesson.setDeleted(true);
            lessonRepository.save(lesson);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Delete lesson success !");
            apiResponse.setData(lesson);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
    }

    @Override
    public APIResponse searchLesson(String keyword, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Specification<Lesson> specification = LessonSpecification.searchByKeyword(keyword);
        Page<Lesson> lessons = lessonRepository.findAll(specification,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search lesson success !");
        apiResponse.setData(lessons);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getLessonByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Lesson> lessons = lessonRepository.findByIsDeletedFalse(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get lesson by page success !");
        apiResponse.setData(lessons);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getLessonByCourseIdAndByPage(Long courseId, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Lesson> lessons = lessonRepository.findByCourseIdAndIsDeletedFalse(courseId,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get lesson by course id success !");
        apiResponse.setData(lessons);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}
