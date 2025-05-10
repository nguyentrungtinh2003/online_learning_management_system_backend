package com.TrungTinhBackend.codearena_backend.Service.Search.Specification;

import com.TrungTinhBackend.codearena_backend.Entity.BlogComment;
import com.TrungTinhBackend.codearena_backend.Entity.PaymentTransaction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PaymentTransactionSpecification {
    public static Specification<PaymentTransaction> searchByKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();
            }
            String pattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("status").as(String.class)), pattern),
                    cb.like(cb.lower(root.get("user").get("username")), pattern)
            );
        };
    }

}
