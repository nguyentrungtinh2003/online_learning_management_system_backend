package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.DTO.UserPointHistoryDTO;
import com.TrungTinhBackend.codearena_backend.Entity.AuditLog;
import com.TrungTinhBackend.codearena_backend.Entity.UserPointHistory;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.AuditLog.AuditLogService;
import com.TrungTinhBackend.codearena_backend.Service.UserPointHistory.UserPointHistoryService;
import com.TrungTinhBackend.codearena_backend.Utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserPointHistoryController {

    @Autowired
    private UserPointHistoryService userPointHistoryService;

    @Autowired
    private AuditLogService auditLogService;

    @PostMapping("/rankings/add")
    public ResponseEntity<APIResponse> addPointHistory(@RequestBody UserPointHistoryDTO userPointHistoryDTO) {
        String username = SecurityUtils.getCurrentUsername();
        auditLogService.addLog(username,"PLUS POINT","Plus "+userPointHistoryDTO.getPoint()+" point");
        return ResponseEntity.ok(userPointHistoryService.addUserPointHistory(userPointHistoryDTO));
    }

    @GetMapping("/user-point-history/{userId}")
    public ResponseEntity<APIResponse> getUserPointHistoryByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userPointHistoryService.getUserPointHistoryByUserId(userId));
    }

    @GetMapping("/rankings/day")
    public ResponseEntity<APIResponse> getTopByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(userPointHistoryService.getTop10ByDate(date));
    }

    @GetMapping("/rankings/week")
    public ResponseEntity<APIResponse> getTopByWeek(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                               @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(userPointHistoryService.getTop10ByWeek(startDate,endDate));
    }

    @GetMapping("/rankings/month")
    public ResponseEntity<APIResponse> getTopByMonth(@RequestParam("month") int month, @RequestParam("year") int year) {
        return ResponseEntity.ok(userPointHistoryService.getTop10ByMonth(month,year));
    }
}
