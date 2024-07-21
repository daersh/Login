package com.daersh.login.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccessRefreshToken {
    private String accessToken;
    private String refreshToken;
}
