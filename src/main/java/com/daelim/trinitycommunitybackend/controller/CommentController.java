package com.daelim.trinitycommunitybackend.controller;

import com.daelim.trinitycommunitybackend.config.CustomAuthorization;
import com.daelim.trinitycommunitybackend.config.JwtProvider;
import com.daelim.trinitycommunitybackend.dto.CommentModifyRequest;
import com.daelim.trinitycommunitybackend.dto.CommentWriteRequest;
import com.daelim.trinitycommunitybackend.dto.Response;
import com.daelim.trinitycommunitybackend.entity.Comment;
import com.daelim.trinitycommunitybackend.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
@Tag(name = "댓글 API")
public class CommentController {
    private final CommentService commentService;
    private final JwtProvider jwtProvider;

    @GetMapping("/{postId}")
    @Operation(summary = "게시글별 댓글 목록 API")
    public Response<Page<Comment>> getCommentByPostId(@PageableDefault(page = 0, size = 10, sort = "commentId", direction = Sort.Direction.ASC) Pageable pageable, @PathVariable Integer postId) {
        return commentService.getCommentByPostId(pageable, postId);
    }

    @GetMapping("/listByUser/{userId}")
    @Operation(summary = "유저별 댓글 목록 API")
    public Response<Page<Comment>> getListByUser(@PageableDefault(page = 0, size = 10, sort = "commentId", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable String userId) {
        return commentService.getListByUser(pageable, userId);
    }

    @PostMapping("/write")
    @Operation(summary = "댓글 작성 API - 로그인 필요, 관리자 가능")
    @CustomAuthorization
    public Response<Comment> writeComment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = CommentWriteRequest.class)
                    )
            )
            @RequestBody Map<String, Object> commentObj, HttpServletRequest request) {
        return commentService.writeComment(commentObj, jwtProvider.getToken(request));
    }

    @PutMapping("/modify")
    @Operation(summary = "댓글 수정 API - 로그인 필요, 관리자 가능")
    @CustomAuthorization
    public Response<Comment> modifyComment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = CommentModifyRequest.class)
                    )
            )
            @RequestBody Map<String, Object> commentObj, HttpServletRequest request) {
        return commentService.modifyComment(commentObj, jwtProvider.getToken(request));
    }

    @DeleteMapping("/delete/{commentId}")
    @Operation(summary = "댓글 삭제 API - 로그인 필요, 관리자 가능")
    @CustomAuthorization
    public Response<Object> deleteComment(@PathVariable Integer commentId, HttpServletRequest request) {
        return commentService.deleteComment(commentId, jwtProvider.getToken(request));
    }
}

