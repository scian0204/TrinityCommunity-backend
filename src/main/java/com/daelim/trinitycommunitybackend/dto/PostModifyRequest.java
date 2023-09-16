package com.daelim.trinitycommunitybackend.dto;

import lombok.Data;

@Data
public class PostModifyRequest {
    private Integer postId;
    private String title;
    private String content;
}
