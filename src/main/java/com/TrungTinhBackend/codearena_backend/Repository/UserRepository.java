package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.Blog;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    public User findByUsernameAndEnabled(String username, boolean b);
    public List<User> findAllByIsDeleted(boolean isDeleted);
    public Page<User> findAllByIsDeletedFalse(Pageable pageable);
    public com.TrungTinhBackend.codearena_backend.Entity.User findByUsername(String username);
    public com.TrungTinhBackend.codearena_backend.Entity.User findByEmail(String email);
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.address) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<User> searchUser(@Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT u.id FROM User u")
    List<Long> getAllUserIds();
}
