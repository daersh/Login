package com.daersh.login.user.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestRegistUser {
    private String userEmail;
    private String userPassword;
    private String userNickname;
}
