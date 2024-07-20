package com.daersh.login.user.controller;


import com.daersh.login.user.aggregate.User;
import com.daersh.login.user.service.LoginService;
import com.daersh.login.user.service.ResponseUserInfo;
import com.daersh.login.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final LoginService loginService;

    @Autowired
    public UserController(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }


    // 회원가입
    @PostMapping("")
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
    @GetMapping("")
    public ResponseEntity<ResponseUserInfo> getUserInfo() {
        ResponseUserInfo user = loginService.getUser();
        return ResponseEntity.ok(user);
    }

    private static ResponseEntity<String> badRequest() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
