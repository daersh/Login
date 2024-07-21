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

@Service
public class LoginServiceImpl implements LoginService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final GetUserInfo getUserInfo;

    @Autowired
    public LoginServiceImpl(UserService userService, PasswordEncoder passwordEncoder, JWTUtil jwtUtil, TokenRepository tokenRepository, GetUserInfo getUserInfo) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
        this.getUserInfo = getUserInfo;
    }

    @Transactional
    @Override
    public HttpServletResponse login(RequestLoginUser requestLoginUser, HttpServletResponse response) {
        try {
            User user = userService.getUser(requestLoginUser.getUserEmail());
            if (!passwordEncoder.matches(requestLoginUser.getUserPassword(), user.getUserPassword())) {
                return null;
            }

            // accesstoken: 약 24시간, refresh: 약 10일
            String accessToken = jwtUtil.createJwt("access", user.getUserEmail(), user.getUsernickname(), user.getUserRole().name(), 86000000L);
            String refreshToken = jwtUtil.createJwt("refresh", user.getUserEmail(), user.getUsernickname(), user.getUserRole().name(), 860000000L);

            UserToken userToken = getUserToken(refreshToken, user);
            tokenRepository.save(userToken);

            response.addHeader("access", "Bearer " + accessToken);
            response.addHeader("refresh", "Bearer " + refreshToken);

        } catch (Exception e) {
            System.err.println("login error occurred");
            System.err.println(e.getMessage());
            return null;
        }

        return response;
    }

    private UserToken getUserToken(String refreshToken, User user) {
        return UserToken.builder()
                .tokenValue(refreshToken)
                .tokenCreateDate(DateParsing.LdtToStr(LocalDateTime.now()))
                .tokenDeleteDate(DateParsing.LdtToStr(LocalDateTime.now().plusDays(7)))
                .userEmail(user.getUserEmail())
                .build();
    }

    @Override
    public ResponseUserInfo getUser() {
        User user = userService.getUser(getUserInfo.getName());
        return new ResponseUserInfo(user);
    }

    @Override
    public AccessRefreshToken processOAuth2User(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String nickname = oAuth2User.getAttribute("nickname");
        String role = "ROLE_USER"; // 기본 역할을 설정합니다. 필요에 따라 다르게 설정할 수 있습니다.

        // 사용자 정보를 DB에 저장하는 로직을 추가합니다.
        RequestRegistUser requestRegistUser = new RequestRegistUser(email, null, nickname);
        userService.saveUser(requestRegistUser);

        // JWT 토큰을 생성합니다.
        String accessToken = jwtUtil.createJwt("access", email, nickname, role, 86000000L);
        String refreshToken = jwtUtil.createJwt("refresh", email, nickname, role, 860000000L);

        return new AccessRefreshToken(accessToken, refreshToken);
    }
}
