package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.CodeExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeExecutionRepository extends JpaRepository<CodeExecution,Long> {
    List<CodeExecution> findByUserId(Long userId);
}
