package com.daersh.login.jwt;

import com.daersh.login.user.repository.TokenRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    // authenticationManager:검증 담당
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final TokenRepository refreshRepo;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, TokenRepository refreshRepo) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshRepo = refreshRepo;
    }



}

