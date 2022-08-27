package com.kunis.linkuni.repository;

import com.kunis.linkuni.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByName(String name);
    Boolean existsByName(String name);
}
