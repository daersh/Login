package com.daersh.login.user.service;

import com.daersh.login.user.aggregate.User;
import lombok.Getter;

@Getter
public class ResponseUserInfo {
    private String userEmail;
    private String userNickname;
    private String userEnrollDate;
    private String userRole;

    public ResponseUserInfo(User user) {
        this.userEmail = user.getUserEmail();
        this.userNickname = user.getUsernickname();
        this.userEnrollDate = user.getUserEnrollDate();
        this.userRole = user.getUserRole().name();
    }
}
