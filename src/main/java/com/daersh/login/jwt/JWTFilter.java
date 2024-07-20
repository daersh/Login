package com.daersh.login.jwt;
//import com.daersh.daersh_project.user.aggregate.User;
//import com.daersh.daersh_project.user.dto.CustomUserDetails;
import com.daersh.login.user.aggregate.CustomUserDetails;
import com.daersh.login.user.aggregate.Role;
import com.daersh.login.user.aggregate.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;


// 요청에 대해서 한번만 받는 필터 extend
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //request에서 헤더 에 키값 빼오기
        String accessToken = request.getHeader("access");
        System.out.println("accessToken = " + accessToken);

        // 토큰 존재여부 및 접두사 확인
        if (accessToken==null || !accessToken.startsWith("Bearer ")){
            System.err.println("token not exist!");
            filterChain.doFilter(request,response);
            return;
        }

        String token = accessToken.split(" ")[1];

        // 토큰 시간 검증
        // 만료 시 다음 필터로 넘기지 않는다.
        // 토큰 만료되었다는 것만 반환
        if (jwtUtil.isExpired(token)){
            System.err.println("expired token!");

            PrintWriter printWriter = response.getWriter();
            printWriter.print("access token expired..");
//            filterChain.doFilter(request,response);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // access token 인지 확인
        String category = jwtUtil.getCategory(token);
        if(!"access".equals(category)){
            PrintWriter printWriter = response.getWriter();
            printWriter.print("This token is not access token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰 검증 완료
        // 세션 생성
        // 토큰 정보 해석
        String userEmail = jwtUtil.getUserEmail(token);
        String userNickname = jwtUtil.getUserNickname(token);
        String role = jwtUtil.getRole(token);
        System.out.println("userId: "+userEmail +" userNickname: "+userNickname +" role: "+role );

        new User();
        User user = User.builder()
                .userEmail(userEmail)
                .usernickname(userNickname)
                .userRole( (Role.valueOf(role)) )
                .build();

        // userdeails에 유저 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails,null,customUserDetails.getAuthorities());

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request,response);
        System.out.println("user access fin");
    }


}
