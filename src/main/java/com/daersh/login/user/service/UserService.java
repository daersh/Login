package com.daersh.login.user.service;


import com.daersh.login.config.DateParsing;
import com.daersh.login.jwt.JWTUtil;
import com.daersh.login.user.aggregate.Role;
import com.daersh.login.user.aggregate.User;
import com.daersh.login.user.controller.RequestLoginUser;
import com.daersh.login.user.controller.RequestRegistUser;
import com.daersh.login.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public String saveUser(RequestRegistUser requestRegistUser) {

        try {

            User user = User.builder()
                    .userEmail(requestRegistUser.getUserEmail())
                    .userPassword(passwordEncoder.encode(requestRegistUser.getUserPassword()))
                    .usernickname(requestRegistUser.getUserNickname())
                    .userRole(Role.ROLE_USER)
                    .userEnrollDate(DateParsing.LdtToStr(LocalDateTime.now()))
                    .userIsDeleted(false)
                    .userDeleteDate(null)
                    .build();

            userRepository.save(user);
        }catch (Exception e){
            System.err.println("Login Failed");
            return null;
        }
        return "success";
    }

    public User getUser(String userEmail) {
        return userRepository.findById(userEmail).orElseThrow();
    }
}