package com.daelim.trinitycommunitybackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class User {
    @Id
    private String userId;
    private String userName;
    private String password;
    private String telNum;
    private Integer isAdmin;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp regDate;
}
