package com.TrungTinhBackend.codearena_backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

public interface UserRepository extends JpaRepository<com.TrungTinhBackend.codearena_backend.Entity.User,Long> {
    public User findByUsernameAndEnabled(String username, boolean b);
    public User findByUsername(String username);
}
