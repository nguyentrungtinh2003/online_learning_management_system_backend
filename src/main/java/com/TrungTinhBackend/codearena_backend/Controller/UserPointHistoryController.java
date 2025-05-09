package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Entity.UserPointHistory;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.UserPointHistory.UserPointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/rankings")
@RequiredArgsConstructor
public class UserPointHistoryController {

    @Autowired
    private UserPointHistoryService userPointHistoryService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addPointHistory(@RequestBody UserPointHistoryDTO dto) {
        return ResponseEntity.ok(userPointHistoryService.addUserPointHistory(dto));
    }

    @GetMapping("/day")
    public ResponseEntity<APIResponse> getTopByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(userPointHistoryService.getTop10ByDate(date));
    }

    @GetMapping("/week")
    public ResponseEntity<APIResponse> getTopByWeek(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                               @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(userPointHistoryService.getTop10ByWeek(startDate,endDate));
    }

    @GetMapping("/month")
    public ResponseEntity<APIResponse> getTopByMonth(@RequestParam("month") int month, @RequestParam("year") int year) {
        return ResponseEntity.ok(userPointHistoryService.getTop10ByMonth(month,year));
    }
}
