package com.daersh.login.user.controller;

import lombok.Getter;

@Getter
public class RequestRegistUser {
    private String userEmail;
    private String userPassword;
    private String userNickname;
}
