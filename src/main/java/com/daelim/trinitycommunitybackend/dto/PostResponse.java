package com.daelim.trinitycommunitybackend.dto;

import com.daelim.trinitycommunitybackend.entity.Post;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class PostResponse {
    private Integer postId;
    private Integer apartId;
    private String userId;
    private String title;
    private String content;
    private Integer viewCount;
    private Timestamp writeDate;

    public PostResponse(Post post) {
        this.postId = post.getPostId();
        this.apartId = post.getApartId();
        this.userId = post.getUserId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.viewCount = post.getViewCount();
        this.writeDate = post.getWriteDate();
    }
}
