package com.daersh.login.user.repository;

import com.daersh.login.user.aggregate.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByUserEmail(String userEmail);
}
