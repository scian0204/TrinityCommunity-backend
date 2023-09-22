package com.daelim.trinitycommunitybackend.dto;

import lombok.Data;

@Data
public class CommentWriteRequest {
    private Integer postId;
    private Integer target;
    private String comment;
}
