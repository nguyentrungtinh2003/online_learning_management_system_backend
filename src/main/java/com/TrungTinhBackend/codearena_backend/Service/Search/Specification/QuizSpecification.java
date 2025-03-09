package com.TrungTinhBackend.codearena_backend.Service.Search.Specification;

import com.TrungTinhBackend.codearena_backend.Entity.Quiz;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class QuizSpecification {
    public static Specification<Quiz> searchByKeyword(String keyword) {
        return (root,query,criteriaBuilder) -> {
            if(keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String pattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("quizName")),pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),pattern)
            );
        };
    }
}
