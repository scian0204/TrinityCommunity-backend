package com.daelim.trinitycommunitybackend.dto;

import lombok.Data;

@Data
public class PostWriteRequest {
    private Integer apartId;
    private String title;
    private String content;
}
