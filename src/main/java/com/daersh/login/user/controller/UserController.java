package com.daersh.login.user.controller;


import com.daersh.login.user.aggregate.User;
import com.daersh.login.user.service.LoginService;
import com.daersh.login.user.service.ResponseUserInfo;
import com.daersh.login.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
public class UserController {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;
    private final UserService userService;
    private final LoginService loginService;

    @Autowired
    public UserController(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }


    // 회원가입
    @PostMapping("/user")
    public ResponseEntity<String> regist( @RequestBody RequestRegistUser requestUser){

        if(requestUser.getUserNickname()==null || requestUser.getUserEmail()==null || requestUser.getUserPassword()==null) {
            System.err.println("null error");
            return badRequest();
        }

        String result = userService.saveUser(requestUser);
        if (result==null){
            return badRequest();
        }
        return ResponseEntity.ok(result);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<String> regist(@RequestBody RequestLoginUser requestLoginUser, HttpServletResponse response){
        if(requestLoginUser.getUserEmail()==null || requestLoginUser.getUserPassword() == null)
            return badRequest();

        HttpServletResponse result = loginService.login(requestLoginUser,response);

        if(result == null)
            return badRequest();
        return ResponseEntity.ok().build();
    }

    // 자신 정보 가져오기
    @GetMapping("user")
    public ResponseEntity<ResponseUserInfo> getUserInfo() {
        ResponseUserInfo user = loginService.getUser();
        return ResponseEntity.ok(user);
    }

    private static ResponseEntity<String> badRequest() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // kakao login 접근
    @GetMapping("/login/kakao")
    public ResponseEntity<String> kakaoLogin(){

        String url ="https://accounts.kakao.com/login/?continue=https%3A%2F%2Fkauth.kakao.com%2Foauth%2Fauthorize%3Fscope%3Dprofile_nickname%2520account_email%26response_type%3Dcode%26state%3D62UzJ8jpPAE34JYZCi6JszXPGW-ZnEmZwrX1lxi3voU%253D%26" +
                "redirect_uri%3Dhttp%253A%252F%252Flocalhost%253A8088%252Flogin%252Foauth2%252Fcode%252Fkakao%26through_account%3Dtrue%26" +
                "client_id%3D"+kakaoClientId +
                "#login";
        System.out.println("login return");
        return ResponseEntity.ok(url);
    }

    @RequestMapping("/auth/success")
    public void kakaoLoginRedirect(@RequestParam String accessToken, @RequestParam String refreshToken, HttpServletResponse response) throws IOException {

        response.addHeader("accessToken", accessToken);
        response.addHeader("refreshToken", refreshToken);
        response.sendRedirect("http://localhost:5173");
    }
}