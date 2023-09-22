package com.daelim.trinitycommunitybackend.dto;

import lombok.Data;

@Data
public class CommentModifyRequest {
    private Integer commentId;
    private Integer postId;
    private Integer target;
    private String comment;
}
