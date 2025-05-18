package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog,Long> {
}
