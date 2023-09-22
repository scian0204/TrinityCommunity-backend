package com.daelim.trinitycommunitybackend.service;

import com.daelim.trinitycommunitybackend.config.JwtProvider;
import com.daelim.trinitycommunitybackend.repository.CommentRepository;
import com.daelim.trinitycommunitybackend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.daelim.trinitycommunitybackend.dto.Error;
import com.daelim.trinitycommunitybackend.dto.Response;
import com.daelim.trinitycommunitybackend.entity.Comment;
import com.daelim.trinitycommunitybackend.entity.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    ObjectMapper objMpr = new ObjectMapper();
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final JwtProvider jwtProvider;

    public Response<Page<Comment>> getCommentByPostId(Pageable pageable, Integer postId) {
        Response<Page<Comment>> res = new Response<>();
        Page<Comment> comments = commentRepository.findAllByPostId(pageable, postId);
        res.setData(comments);
        return res;
    }

    public Response<Comment> writeComment(Map<String, Object> commentObj, String token) {
        Comment comment = objMpr.convertValue(commentObj, Comment.class);
        comment.setUserId(jwtProvider.getUserId(token));
        Optional<Post> postOptional = postRepository.findById(comment.getPostId());
        Response<Comment> res = new Response<>();
        if (postOptional.isPresent()) {
            res.setData(commentRepository.save(comment));
        } else {
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("해당 게시물이 존재하지 않음");
            res.setError(error);
        }

        return res;
    }

    public Response<Comment> modifyComment(Map<String, Object> commentObj, String token) {
        Comment comment = objMpr.convertValue(commentObj, Comment.class);
        comment.setUserId(jwtProvider.getUserId(token));

        Optional<Comment> commentOptional = commentRepository.findById(comment.getCommentId());
        Response<Comment> res = new Response<>();

        if (commentOptional.isPresent()) {
            if (postRepository.findById(comment.getPostId()).isPresent()) {
                if (commentOptional.get().getUserId().equals(comment.getUserId()) || jwtProvider.isAdmin(token)) {
                    comment.setWriteDate(commentOptional.get().getWriteDate());
                    res.setData(commentRepository.save(comment));
                } else {
                    Error error = new Error();
                    error.setErrorId(2);
                    error.setMessage("해당 댓글의 작성자와 로그인된 아이디가 다름");
                    res.setError(error);
                }
            } else {
                Error error = new Error();
                error.setErrorId(1);
                error.setMessage("해당 게시물이 존재하지 않음");
                res.setError(error);
            }
        } else {
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("해당 댓글이 존재하지 않음");
            res.setError(error);
        }

        return res;
    }

    public Response<Object> deleteComment(Integer commentId, String token) {
        Response<Object> res = new Response<>();
        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        if (commentOptional.isPresent()) {
            if (commentOptional.get().getUserId().equals(jwtProvider.getUserId(token)) || jwtProvider.isAdmin(token)) {
                commentRepository.delete(commentOptional.get());
            } else {
                Error error = new Error();
                error.setErrorId(1);
                error.setMessage("해당 댓글의 작성자와 로그인된 아이디가 다름");
                res.setError(error);
            }
        } else {
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("해당 댓글이 존재하지 않음");
            res.setError(error);
        }

        return res;
    }

    public Response<Page<Comment>> getListByUser(Pageable pageable, String userId) {
        Response<Page<Comment>> res = new Response<>();
        Page<Comment> comments = commentRepository.findAllByUserId(pageable, userId);
        res.setData(comments);

        return res;
    }
}
