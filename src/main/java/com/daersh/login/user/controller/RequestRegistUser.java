package com.daersh.login.user.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class RequestRegistUser {
    private String userEmail;
    private String userPassword;
    private String userNickname;
}
