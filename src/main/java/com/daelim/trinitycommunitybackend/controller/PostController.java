package com.daelim.trinitycommunitybackend.controller;

import com.daelim.trinitycommunitybackend.config.CustomAuthorization;
import com.daelim.trinitycommunitybackend.config.JwtProvider;
import com.daelim.trinitycommunitybackend.dto.PostModifyRequest;
import com.daelim.trinitycommunitybackend.dto.PostResponse;
import com.daelim.trinitycommunitybackend.dto.PostWriteRequest;
import com.daelim.trinitycommunitybackend.dto.Response;
import com.daelim.trinitycommunitybackend.entity.Post;
import com.daelim.trinitycommunitybackend.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
@Tag(name = "게시글 API")
public class PostController {
    private final PostService postService;
    private final JwtProvider jwtProvider;

    @GetMapping("/list")
    @Operation(summary = "게시글 전체 목록 API")
    public Response<Page<Post>> getPostList(@PageableDefault(page = 0, size = 10, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getPostList(pageable);
    }

    @GetMapping("/list/{apartId}")
    @Operation(summary = "아파트별 게시글 목록 API")
    public Response<Page<Post>> getPostListByApartId(@PageableDefault(page = 0, size = 10, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Integer apartId) {
        return postService.getPostListByApartId(pageable, apartId);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 상세 API")
    public Response<PostResponse> getPostByPostId(@PathVariable Integer postId) {
        return postService.getPostByPostId(postId);
    }

    @GetMapping("/listByLike/{query}")
    @Operation(summary = "게시글 검색 API", description = "제목, 내용, 작성자 아이디 Like로 검색")
    public Response<Page<Post>> getListByLike(@PageableDefault(page = 0, size = 10, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable String query) {
        return postService.getListByLike(pageable, "%"+query+"%");
    }

    @GetMapping("/listByUser/{userId}")
    @Operation(summary = "유저별 게시글 목록 API")
    public  Response<Page<Post>> getListByUser(@PageableDefault(page = 0, size = 10, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable String userId) {
        return postService.getListByUser(pageable, userId);
    }

    @PostMapping("/write")
    @Operation(summary = "게시글 작성 API - 로그인 필요, 관리자 가능")
    @CustomAuthorization
    public Response<Post> writePost(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = PostWriteRequest.class)
                    )
            )
            @RequestBody Map<String, Object> postObj, HttpServletRequest request) {
        return postService.writePost(postObj, jwtProvider.getToken(request));
    }

    @PutMapping("/update")
    @Operation(summary = "게시글 수정 API - 로그인 필요, 관리자 가능")
    @CustomAuthorization
    public Response<Post> updatePost(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = PostModifyRequest.class)
                    )
            )
            @RequestBody Map<String, Object> postObj,
            HttpServletRequest request
    ) {
        return postService.updatePost(postObj, jwtProvider.getToken(request));
    }

    @DeleteMapping("/delete/{postId}")
    @Operation(summary = "게시글 삭제 API - 로그인 필요, 관리자 가능")
    @CustomAuthorization
    public Response<Object> deletePost(@PathVariable Integer postId, HttpServletRequest request) {
        return postService.deletePost(postId, jwtProvider.getToken(request));
    }
}
