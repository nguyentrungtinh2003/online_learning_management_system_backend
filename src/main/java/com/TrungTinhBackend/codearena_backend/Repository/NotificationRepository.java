package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.Chat;
import com.TrungTinhBackend.codearena_backend.Entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByReceiver_IdOrderByCreatedAtDesc(Long userId);
    @Query("SELECT n FROM Notification n WHERE " +
            "LOWER(n.message) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Notification> searchNotification(@Param("keyword") String keyword, Pageable pageable);
}
