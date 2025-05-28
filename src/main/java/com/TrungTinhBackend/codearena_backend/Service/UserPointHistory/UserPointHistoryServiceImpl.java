package com.TrungTinhBackend.codearena_backend.Service.UserPointHistory;

import com.TrungTinhBackend.codearena_backend.DTO.UserPointHistoryDTO;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Entity.UserPointHistory;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.UserPointHistoryRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.User.UserService;
import com.TrungTinhBackend.codearena_backend.Service.WebSocket.WebSocketSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Optional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserPointHistoryServiceImpl implements UserPointHistoryService{

    @Autowired
    private UserPointHistoryRepository userPointHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebSocketSender webSocketSender;

    @Autowired
    @Lazy
    private UserService userService;

    public UserPointHistoryServiceImpl(UserPointHistoryRepository userPointHistoryRepository, UserRepository userRepository, WebSocketSender webSocketSender,  @Lazy UserService userService) {
        this.userPointHistoryRepository = userPointHistoryRepository;
        this.userRepository = userRepository;
        this.webSocketSender = webSocketSender;
        this.userService = userService;
    }

    @Override
    public APIResponse addUserPointHistory(UserPointHistoryDTO userPointHistoryDTO) {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findById(userPointHistoryDTO.getUserId()).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        user.setPoint(user.getPoint() + userPointHistoryDTO.getPoint());
        // Ngăn không cho point bị âm
        if (user.getPoint() < 0) {
            throw new IllegalArgumentException("Tổng điểm không được âm");
        }

        user.setRankEnum(userService.calculateRank(user.getPoint()));
        userRepository.save(user);

        webSocketSender.sendUserInfo(user);
        
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
    public APIResponse getUserPointHistoryByUserId(Long userId) {
        APIResponse apiResponse = new APIResponse();

        List<UserPointHistory> userPointHistories = userPointHistoryRepository.findByUserId(userId);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get user point history by userId success");
        apiResponse.setData(userPointHistories);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getTop10ByDate(LocalDate date) {
        APIResponse apiResponse = new APIResponse();

        List<UserPointHistory> userPointHistories = userPointHistoryRepository.findTop10ByDateOrderByPointDesc(date).stream()
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
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(UserPointHistory::getPoint).reversed())
                .limit(10)
                .collect(Collectors.toList());

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get top 10 by date success");
        apiResponse.setData(userPointHistories);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    //Plus point date
    @Scheduled(cron = "0 0 0 * * ?") // chạy lúc 00:00 mỗi ngày
    public void rewardTop3Daily() {
        LocalDate date = LocalDate.now().minusDays(1); // lấy dữ liệu ngày hôm qua

        List<UserPointHistory> top3 = userPointHistoryRepository.findTop10ByDateOrderByPointDesc(date).stream()
                .collect(Collectors.groupingBy(h -> h.getUser().getId(),
                        Collectors.collectingAndThen(
                                Collectors.reducing((h1, h2) -> {
                                    h1.setPoint(h1.getPoint() + h2.getPoint());
                                    return h1;
                                }),
                                Optional::get
                        )))
                .values().stream()
                .sorted(Comparator.comparingLong(UserPointHistory::getPoint).reversed())
                .limit(3)
                .toList();

        Long[] rewards = {10L, 8L, 5L};

        for (int i = 0; i < top3.size(); i++) {
            User user = top3.get(i).getUser();
            user.setPoint(user.getPoint() + rewards[i]);
            user.setRankEnum(userService.calculateRank(user.getPoint()));
            userRepository.save(user);
            webSocketSender.sendUserInfo(user);

            UserPointHistory rewardHistory = new UserPointHistory();
            rewardHistory.setUser(user);
            rewardHistory.setPoint(rewards[i]);
            rewardHistory.setDate(LocalDate.now()); // ngày cộng thưởng
            userPointHistoryRepository.save(rewardHistory);
        }
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

    @Scheduled(cron = "0 0 0 * * MON") // 00:00 mỗi thứ Hai
    public void rewardTop3Weekly() {
        LocalDate endDate = LocalDate.now().minusDays(1); // hôm qua (chủ nhật)
        LocalDate startDate = endDate.minusDays(6); // từ thứ hai trước

        List<UserPointHistory> top3 = userPointHistoryRepository.findTop10ByWeek(startDate, endDate).stream()
                .collect(Collectors.groupingBy(h -> h.getUser().getId(),
                        Collectors.collectingAndThen(
                                Collectors.reducing((h1, h2) -> {
                                    h1.setPoint(h1.getPoint() + h2.getPoint());
                                    return h1;
                                }),
                                Optional::get
                        )))
                .values().stream()
                .sorted(Comparator.comparingLong(UserPointHistory::getPoint).reversed())
                .limit(3)
                .toList();

        Long[] rewards = {30L, 25L, 20L};

        for (int i = 0; i < top3.size(); i++) {
            User user = top3.get(i).getUser();
            user.setPoint(user.getPoint() + rewards[i]);
            user.setRankEnum(userService.calculateRank(user.getPoint()));
            userRepository.save(user);
            webSocketSender.sendUserInfo(user);

            UserPointHistory rewardHistory = new UserPointHistory();
            rewardHistory.setUser(user);
            rewardHistory.setPoint(rewards[i]);
            rewardHistory.setDate(LocalDate.now());
            userPointHistoryRepository.save(rewardHistory);
        }
    }

    @Override
    public APIResponse getTop10ByMonth(int month, int year) {
        APIResponse apiResponse = new APIResponse();

        List<UserPointHistory> userPointHistories = userPointHistoryRepository.findTop10ByMonth(month, year).stream()
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
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(UserPointHistory::getPoint).reversed())
                .limit(10)
                .collect(Collectors.toList());

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get top 10 by month, year success");
        apiResponse.setData(userPointHistories);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Scheduled(cron = "0 0 0 1 * ?") // 00:00 ngày 1 mỗi tháng
    public void rewardTop3Monthly() {
        LocalDate now = LocalDate.now().minusDays(1); // hôm qua là cuối tháng
        int month = now.getMonthValue();
        int year = now.getYear();

        List<UserPointHistory> top3 = userPointHistoryRepository.findTop10ByMonth(month, year).stream()
                .collect(Collectors.groupingBy(h -> h.getUser().getId(),
                        Collectors.collectingAndThen(
                                Collectors.reducing((h1, h2) -> {
                                    h1.setPoint(h1.getPoint() + h2.getPoint());
                                    return h1;
                                }),
                                Optional::get
                        )))
                .values().stream()
                .sorted(Comparator.comparingLong(UserPointHistory::getPoint).reversed())
                .limit(3)
                .toList();

        Long[] rewards = {50L, 40L, 30L};

        for (int i = 0; i < top3.size(); i++) {
            User user = top3.get(i).getUser();
            user.setPoint(user.getPoint() + rewards[i]);
            user.setRankEnum(userService.calculateRank(user.getPoint()));
            userRepository.save(user);
            webSocketSender.sendUserInfo(user);

            UserPointHistory rewardHistory = new UserPointHistory();
            rewardHistory.setUser(user);
            rewardHistory.setPoint(rewards[i]);
            rewardHistory.setDate(LocalDate.now());
            userPointHistoryRepository.save(rewardHistory);
        }
    }

}
