package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.Blog;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Enum.RoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
    public User findByUsernameAndEnabled(String username, boolean b);
    public List<User> findAllByIsDeleted(boolean isDeleted);
    public Page<User> findAllByIsDeletedFalse(Pageable pageable);
    public com.TrungTinhBackend.codearena_backend.Entity.User findByUsername(String username);
    public com.TrungTinhBackend.codearena_backend.Entity.User findByEmail(String email);
    @Query("SELECT u.id FROM User u")
    List<Long> getAllUserIds();
    List<User> findByRoleEnum(RoleEnum roleEnum);
}
