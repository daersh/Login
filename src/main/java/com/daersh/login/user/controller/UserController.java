package com.daersh.login.user.controller;


import com.daersh.login.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // 회원가입
    @PostMapping("")
    public ResponseEntity<String> regist( @RequestBody RequestRegistUser requestUser){

        if(requestUser.getUserNickname()==null || requestUser.getUserEmail()==null || requestUser.getUserPassword()==null) {
            System.err.println("null error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String result = userService.saveUser(requestUser);
        if (result==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(result);
    }

}
