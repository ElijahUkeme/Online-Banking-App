package com.elijah.onlinebankingapp.security.repository;

import com.elijah.onlinebankingapp.security.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel,Long> {
    UserModel findByUsername(String username);
}
