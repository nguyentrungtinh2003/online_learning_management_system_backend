package com.TrungTinhBackend.codearena_backend.Service.Search.Specification;

import com.TrungTinhBackend.codearena_backend.Entity.Lesson;
import org.springframework.data.jpa.domain.Specification;

public class LessonSpecification {
    public static Specification<Lesson> searchByKeyword(String keyword) {
        return (root,query,criteriaBuilder) -> {
            if(keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String pattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lessonName")),pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),pattern)
            );
        };
    }
}
