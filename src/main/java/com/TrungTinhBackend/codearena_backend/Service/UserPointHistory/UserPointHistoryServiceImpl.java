package com.TrungTinhBackend.codearena_backend.Service.UserPointHistory;

import com.TrungTinhBackend.codearena_backend.DTO.UserPointHistoryDTO;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Entity.UserPointHistory;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.UserPointHistoryRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPointHistoryServiceImpl implements UserPointHistoryService{

    @Autowired
    private UserPointHistoryRepository userPointHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    public UserPointHistoryServiceImpl(UserPointHistoryRepository userPointHistoryRepository, UserRepository userRepository) {
        this.userPointHistoryRepository = userPointHistoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public APIResponse addUserPointHistory(UserPointHistoryDTO userPointHistoryDTO) {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findById(userPointHistoryDTO.getUserId()).orElseThrow(
                () -> new NotFoundException("User not found")
        );
        
        UserPointHistory userPointHistory = new UserPointHistory();
        userPointHistory.setUser(user);
        userPointHistory.setPoint(userPointHistoryDTO.getPoint());
        userPointHistory.setDate(LocalDate.now());

        userPointHistoryRepository.save(userPointHistory);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Save user point history success");
        apiResponse.setData(userPointHistory);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getTop10ByDate(LocalDate date) {
        APIResponse apiResponse = new APIResponse();

        List<UserPointHistory> userPointHistories = userPointHistoryRepository.findTop10ByDateOrderByPointDesc(date);
        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get top 10 by date success");
        apiResponse.setData(userPointHistories);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getTop10ByWeek(LocalDate startDate, LocalDate endDate) {
        APIResponse apiResponse = new APIResponse();

        List<UserPointHistory> userPointHistories = userPointHistoryRepository.findTop10ByWeek(startDate, endDate).stream()
                .collect(Collectors.groupingBy(
                        h -> h.getUser().getId(),
                        Collectors.collectingAndThen(
                                Collectors.reducing((h1, h2) -> {
                                    h1.setPoint(h1.getPoint() + h2.getPoint());
                                    return h1;
                                }),
                                optional -> optional.orElse(null)
                        )
                ))
                .values().stream()
                .filter(h -> h != null)
                .sorted(Comparator.comparingLong(UserPointHistory::getPoint).reversed())
                .limit(10)
                .collect(Collectors.toList());

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get top 10 by week success");
        apiResponse.setData(userPointHistories);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getTop10ByMonth(int month, int year) {
        APIResponse apiResponse = new APIResponse();

        List<UserPointHistory> userPointHistories = userPointHistoryRepository.findTop10ByMonth(month,year);
        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get top 10 by month, year success");
        apiResponse.setData(userPointHistories);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}
