package com.TrungTinhBackend.codearena_backend.Repository;

import com.TrungTinhBackend.codearena_backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<com.TrungTinhBackend.codearena_backend.Entity.User,Long> {
    public User findByUsernameAndEnabled(String username, boolean b);
    public List<User> findAllByIsDeleted(boolean isDeleted);
    public com.TrungTinhBackend.codearena_backend.Entity.User findByUsername(String username);
    public com.TrungTinhBackend.codearena_backend.Entity.User findByEmail(String email);
}
