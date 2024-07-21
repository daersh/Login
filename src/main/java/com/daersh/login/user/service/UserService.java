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

public interface UserService {
    public String saveUser(RequestRegistUser requestRegistUser);

    public User getUser(String userEmail);
}