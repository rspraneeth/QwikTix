package com.qwiktix.repository;

import com.qwiktix.data.PasswordToken;
import com.qwiktix.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRepository extends JpaRepository<PasswordToken, Integer> {
    PasswordToken findByUser(User user);
}
