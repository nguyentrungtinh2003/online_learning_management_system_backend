package com.TrungTinhBackend.codearena_backend.Service.Search.Specification;

import com.TrungTinhBackend.codearena_backend.Entity.Question;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class QuestionSpecification {
    public static Specification<Question> searchByKeyword(String keyword) {
        return (root,query,criteriaBuilder) -> {
            if(keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String pattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("questionName")),pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("answerA")),pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("answerB")),pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("answerC")),pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("answerD")),pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("answerCorrect")),pattern)
            );
        };
    }
}
