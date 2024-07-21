package com.daersh.login.config;

import com.daersh.login.user.service.LoginService;
import com.daersh.login.config.AccessRefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final LoginService loginService;
//    private static final String URI = "/auth/success";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // accessToken, refreshToken 발급
        AccessRefreshToken tokens = loginService.processOAuth2User(oAuth2User);

        // 토큰 전달을 위한 redirect
//        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
//                .queryParam("accessToken", tokens.getAccessToken())
//                .queryParam("refreshToken", tokens.getRefreshToken())
//                .build().toUriString();
        response.addHeader("accessToken", tokens.getAccessToken());
        response.addHeader("refreshToken", tokens.getRefreshToken());

//        response.sendRedirect(redirectUrl);
    }
}
