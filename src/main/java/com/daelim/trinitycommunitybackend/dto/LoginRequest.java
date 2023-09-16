package com.daelim.trinitycommunitybackend.dto;

import lombok.Data;

@Data
public class LoginRequest {
    String userId;
    String password;
}
