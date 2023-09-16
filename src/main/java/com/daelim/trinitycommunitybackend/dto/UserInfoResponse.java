package com.daelim.trinitycommunitybackend.dto;

import com.daelim.trinitycommunitybackend.entity.User;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserInfoResponse {
    private String userId;
    private String userName;
    private String telNum;
    private Integer isAdmin;
    private Timestamp regDate;

    public UserInfoResponse(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.telNum = user.getTelNum();
        this.isAdmin = user.getIsAdmin();
        this.regDate = user.getRegDate();
    }
}
