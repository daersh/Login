package com.daersh.login.user.service;

import com.daersh.login.config.AccessRefreshToken;
import com.daersh.login.config.DateParsing;
import com.daersh.login.config.GetUserInfo;
import com.daersh.login.jwt.JWTUtil;
import com.daersh.login.user.aggregate.User;
import com.daersh.login.user.aggregate.UserToken;
import com.daersh.login.user.controller.RequestLoginUser;
import com.daersh.login.user.controller.RequestRegistUser;
import com.daersh.login.user.repository.TokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface LoginService {

    public HttpServletResponse login(RequestLoginUser requestLoginUser, HttpServletResponse response);

    public ResponseUserInfo getUser();

    public AccessRefreshToken processOAuth2User(OAuth2User oAuth2User);
}
