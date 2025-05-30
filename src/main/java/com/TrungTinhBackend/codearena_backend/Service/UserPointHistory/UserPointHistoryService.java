package com.TrungTinhBackend.codearena_backend.Service.UserPointHistory;

import com.TrungTinhBackend.codearena_backend.DTO.UserPointHistoryDTO;
import com.TrungTinhBackend.codearena_backend.Entity.UserPointHistory;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface UserPointHistoryService {
    public APIResponse addUserPointHistory(UserPointHistoryDTO userPointHistoryDTO);
    public APIResponse getUserPointHistoryByUserId(Long userId);
    public APIResponse getTop10ByDate(LocalDate date);
    public APIResponse getTop10ByWeek(LocalDate startDate, LocalDate endDate);
    public APIResponse getTop10ByMonth(int month, int year);
}
