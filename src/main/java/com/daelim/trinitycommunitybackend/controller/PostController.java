package com.daelim.trinitycommunitybackend.controller;

import com.daelim.trinitycommunitybackend.config.JwtProvider;
import com.daelim.trinitycommunitybackend.dto.PostModifyRequest;
import com.daelim.trinitycommunitybackend.dto.PostResponse;
import com.daelim.trinitycommunitybackend.dto.PostWriteRequest;
import com.daelim.trinitycommunitybackend.dto.Response;
import com.daelim.trinitycommunitybackend.entity.Post;
import com.daelim.trinitycommunitybackend.service.PostService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class PostController {
    private final PostService postService;
    private final JwtProvider jwtProvider;

    @GetMapping("/list")
    public Response<Page<Post>> getPostList(@PageableDefault(page = 0, size = 10, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getPostList(pageable);
    }

    @GetMapping("/list/{apartId}")
    public Response<Page<Post>> getPostListByApartId(@PageableDefault(page = 0, size = 10, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Integer apartId) {
        return postService.getPostListByApartId(pageable, apartId);
    }

    @GetMapping("/{postId}")
    public Response<PostResponse> getPostByPostId(@PathVariable Integer postId) {
        return postService.getPostByPostId(postId);
    }

    @GetMapping("/listByLike/{query}")
    public Response<Page<Post>> getListByLike(@PageableDefault(page = 0, size = 10, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable String query) {
        return postService.getListByLike(pageable, "%"+query+"%");
    }

    @GetMapping("/listByUser/{userId}")
    public  Response<Page<Post>> getListByUser(@PageableDefault(page = 0, size = 10, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable String userId) {
        return postService.getListByUser(pageable, userId);
    }

    @PostMapping("/write")
    public Response<Post> writePost(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = PostWriteRequest.class)
                    )
            )
            @RequestBody Map<String, Object> postObj, HttpServletRequest request) {
        Response<Post> res = new Response<>();
        if (jwtProvider.validateToken(request)) {
            res = postService.writePost(postObj);
        } else {
            res.setError(jwtProvider.returnLoginError());
        }
        return res;
    }

    @PutMapping("/update")
    public Response<Post> updatePost(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = PostModifyRequest.class)
                    )
            )
            @RequestBody Map<String, Object> postObj,
            HttpServletRequest request
    ) {
        Response<Post> res = new Response<>();
        if (jwtProvider.validateToken(request)) {
            res = postService.updatePost(postObj, jwtProvider.getToken(request));
        } else {
            res.setError(jwtProvider.returnLoginError());
        }
        return res;
    }

    @DeleteMapping("/delete/{postId}")
    public Response<Object> deletePost(@PathVariable Integer postId, HttpServletRequest request) {
        Response<Object> res = new Response<>();
        if (jwtProvider.validateToken(request)) {
            res = postService.deletePost(postId, jwtProvider.getToken(request));
        } else {
            res.setError(jwtProvider.returnLoginError());
        }
        return res;
    }
}
