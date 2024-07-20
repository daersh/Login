package com.daersh.login.user.repository;

import com.daersh.login.user.aggregate.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<UserToken, Integer> {
    UserToken findByUserEmail(String userEmail);
}
