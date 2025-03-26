package com.TrungTinhBackend.codearena_backend.Service.Process;

import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.Process;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Enum.ProcessEnum;
import com.TrungTinhBackend.codearena_backend.Repository.ProcessRepository;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProcessServiceImpl implements ProcessService{

    @Autowired
    private ProcessRepository processRepository;

    public ProcessServiceImpl(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    @Override
    public APIResponse updateProcess(Long userId, Long courseId, Long lessonId) throws Exception {
        APIResponse apiResponse = new APIResponse();

            Process lessonProcess = processRepository.findByUserIdAndLessonId(userId, lessonId);
            lessonProcess.setCompletionPercent(100);
            processRepository.save(lessonProcess);

            // 2. Tính số bài đã hoàn thành / tổng số bài
            long totalLessons = processRepository.countByCourseIdAndLessonIsNotNull(courseId);
            long completedLessons = processRepository.countByCourseIdAndUserIdAndCompletionPercent(courseId, userId, 100);

            Process courseProcess = processRepository.findByUserIdAndCourseIdAndLessonIsNull(userId, courseId);

            int newCompletionPercent = (int) ((completedLessons * 100)/totalLessons);
            courseProcess.setCompletionPercent(newCompletionPercent);

            if(newCompletionPercent == 100) {
                courseProcess.setStatus(ProcessEnum.COMPLETED);
            }

            processRepository.save(courseProcess);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Update process success !");
            apiResponse.setData(courseProcess);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
    }

}
