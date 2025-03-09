package com.TrungTinhBackend.codearena_backend.Service.Search.Specification;

import com.TrungTinhBackend.codearena_backend.Entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSpecification {
    public static Specification<User> searchByKeyword(String keyword) {
        return (root,query,criteriaBuilder) -> {
            if(keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String pattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("username")),pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")),pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("address")),pattern)
            );
        };
    }
}
