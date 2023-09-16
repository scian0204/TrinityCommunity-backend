package com.daelim.trinitycommunitybackend.service;

import com.daelim.trinitycommunitybackend.config.JwtProvider;
import com.daelim.trinitycommunitybackend.dto.Error;
import com.daelim.trinitycommunitybackend.dto.PostModifyRequest;
import com.daelim.trinitycommunitybackend.dto.PostResponse;
import com.daelim.trinitycommunitybackend.dto.Response;
import com.daelim.trinitycommunitybackend.entity.Post;
import com.daelim.trinitycommunitybackend.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    ObjectMapper objMpr = new ObjectMapper();
    private final PostRepository postRepository;
    private final JwtProvider jwtProvider;

    public Response<Page<Post>> getPostList(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        Response<Page<Post>> res = new Response<>();
        res.setData(posts);
        return res;
    }

    public Response<Post> writePost(Map<String, Object> postObj) {
        Post post = objMpr.convertValue(postObj, Post.class);
        Response<Post> res = new Response<>();

        res.setData(postRepository.save(post));
        return res;
    }

    public Response<Page<Post>> getPostListByApartId(Pageable pageable, Integer apartId) {
        Response<Page<Post>> res = new Response<>();
        res.setData(postRepository.findAllByApartId(pageable, apartId));

        return res;
    }

    public Response<PostResponse> getPostByPostId(Integer postId) {
        Response<PostResponse> res = new Response<>();
        Post post = postRepository.getReferenceById(postId);
        post.setViewCount(post.getViewCount()+1);
        res.setData(new PostResponse(postRepository.save(post)));
        return res;
    }

    public Response<Object> deletePost(Integer postId, String token) {
        Response<Object> res = new Response<>();
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()){
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("해당 게시물이 존재하지 않음");
            res.setError(error);
        } else if (!postOptional.get().getUserId().equals(jwtProvider.getUserId(token)) || !jwtProvider.isAdmin(token)) {
            Error error = new Error();
            error.setErrorId(1);
            error.setMessage("해당 게시물을 작성한 유저가 아님");
            res.setError(error);
        } else {
            postRepository.delete(postOptional.get());
        }

        return res;
    }

    public Response<Post> updatePost(Map<String, Object> postObj, String token) {
        Response<Post> res = new Response<>();
        PostModifyRequest postModifyRequest = objMpr.convertValue(postObj, PostModifyRequest.class);

        Optional<Post> postOptional = postRepository.findById(postModifyRequest.getPostId());
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (post.getUserId().equals(jwtProvider.getUserId(token)) || jwtProvider.isAdmin(token)) {
                post.setTitle(postModifyRequest.getTitle());
                post.setContent(postModifyRequest.getContent());
                res.setData(postRepository.save(post));
            } else {
                Error error = new Error();
                error.setErrorId(1);
                error.setMessage("해당 게시글의 작성자와 로그인된 아이디가 다름");
                res.setError(error);
            }
        } else {
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("해당 게시글이 존재하지 않음");
            res.setError(error);
        }

        return res;
    }

    public Response<Page<Post>> getListByLike(Pageable pageable, String query) {
        Response<Page<Post>> res = new Response<>();
        Page<Post> posts = postRepository.findAllByTitleLikeIgnoreCaseOrContentLikeIgnoreCaseOrUserIdLike(pageable, query, query, query);
        res.setData(posts);

        return res;
    }

    public Response<Page<Post>> getListByUser(Pageable pageable, String userId) {
        Response<Page<Post>> res = new Response<>();
        Page<Post> posts = postRepository.findAllByUserId(pageable, userId);
        res.setData(posts);

        return res;
    }
}
