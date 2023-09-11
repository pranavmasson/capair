package com.capair.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.capair.api.model.User;

public interface UserDao extends JpaRepository<User, Integer> {

    User findByEmail(String email);
    boolean existsByEmail(String email);

}
