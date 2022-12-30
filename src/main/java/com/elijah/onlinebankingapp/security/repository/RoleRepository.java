package com.elijah.onlinebankingapp.security.repository;

import com.elijah.onlinebankingapp.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
