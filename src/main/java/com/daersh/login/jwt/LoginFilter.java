package com.daersh.login.jwt;

import com.daersh.login.user.aggregate.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    // authenticationManager:검증 담당
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
//    private final RefreshRepo refreshRepo;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil
//            , RefreshRepo refreshRepo
            ) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
//        this.refreshRepo = refreshRepo;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res)
            throws AuthenticationException {

        System.out.println("attemptAuthentication");
        // id , pwd 추출
        String userId = obtainUsername(req);
        String userPwd = obtainPassword(req);
        System.out.println("userId = " + userId);
        System.out.println("userPwd = " + userPwd);
        // 시큐리티에서 아이디, 비밀번호를 검증하기 위해서는 토큰에 담아야한다.
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userId,userPwd, null);

        System.out.println("next ");

        //토큰에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(token);
    }


    // login 성공
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        System.out.println("successfulAunthentication");
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends  GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        // getUsername = email
        String userEmail = customUserDetails.getUsername();
        String userNickname = customUserDetails.getUserNickname();
        String role = auth.getAuthority();

        // 프론트로 반환할 토큰 생성
        String access = jwtUtil.createJwt("access",userEmail,userNickname,role,86400000L);
        String refresh = jwtUtil.createJwt("refresh",userEmail,userNickname,role,86400000L);

        // refresh token db에 저장
        addRefresh(userNickname,refresh,86400000L);

        // Http 인증 방식은 RFC 7235 정의에 따라 Bearer 인증 헤더 형태를 가져야 한다.
        res.addHeader("access","Bearer " + access);
        // 쿠키에는 리프레스 담기
        res.addCookie(createCookie("refresh",refresh));
    }

    // Refresh 토큰을 DB에 저장히기 위한 메서드
    private void addRefresh(String userNickname, String refreshToken , long expireMs){

        Date date = new Date(System.currentTimeMillis()+expireMs);

//        Refresh refresh =Refresh.builder()
//                            .userId(userId)
//                            .refresh(refreshToken)
//                            .expiration(date)
//                            .build();

//        System.out.println("refresh = " + refresh);

//        refreshRepo.save(refresh);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
//        cookie.setSecure(true);   //https 인 경우 사용
        cookie.setPath("/");   //쿠키 지정 범위
        cookie.setHttpOnly(true);

        return cookie;
    }


    // login 실패
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, AuthenticationException failed) throws IOException, ServletException {

        System.err.println("login failed");
        res.setStatus(400);
    }

}
