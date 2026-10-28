package com.aimbitious.job.repository;

import com.aimbitious.job.domain.UserRole;
import com.aimbitious.job.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User ,Long> {

    User findByEmail(String email);

    List<User> findByRole(UserRole role);

    boolean existsByEmail(String email);
}
