package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.UserPointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface UserPointHistoryRepository extends JpaRepository<UserPointHistory,Long> {
    List<UserPointHistory> findTop10ByDateOrderByPointDesc(LocalDate date);

    @Query("SELECT h FROM UserPointHistory h WHERE h.date BETWEEN :startDate AND :endDate ORDER BY h.point DESC")
    List<UserPointHistory> findTop10ByWeek(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT h FROM UserPointHistory h WHERE EXTRACT(MONTH FROM h.date) = :month AND EXTRACT(YEAR FROM h.date) = :year ORDER BY h.point DESC")
    List<UserPointHistory> findTop10ByMonth(@Param("month") int month, @Param("year") int year);
}
