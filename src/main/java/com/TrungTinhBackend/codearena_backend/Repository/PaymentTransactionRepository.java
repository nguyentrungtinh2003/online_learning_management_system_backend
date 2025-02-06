package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction,Long> {
}
