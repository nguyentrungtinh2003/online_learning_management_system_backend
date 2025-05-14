package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.PaymentTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction,Long> {
    List<PaymentTransaction> findByUserId(Long userId);
    @Query("SELECT SUM(p.amount) FROM PaymentTransaction p")
    Double getTotalAmount();
    Page<PaymentTransaction> findAll(Specification<PaymentTransaction> specification, Pageable pageable);
}
