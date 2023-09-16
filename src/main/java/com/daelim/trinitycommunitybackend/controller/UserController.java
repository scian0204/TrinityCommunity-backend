package com.daelim.trinitycommunitybackend.controller;

import com.daelim.trinitycommunitybackend.config.JwtProvider;
import com.daelim.trinitycommunitybackend.dto.LoginRequest;
import com.daelim.trinitycommunitybackend.dto.Response;
import com.daelim.trinitycommunitybackend.dto.UserInfoResponse;
import com.daelim.trinitycommunitybackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtProvider jwtProvider;


    @Operation(summary = "로그인 여부 확인 API")
    @ApiResponse(description = "토큰 검증")
    @GetMapping("/isLogin")
    public Response<Boolean> isLogin(HttpServletRequest request) {
        Response<Boolean> res = new Response<>();
        res.setData(jwtProvider.validateToken(request));
        return res;
    }

    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public Response<String> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = LoginRequest.class)
                    )
            )
            @RequestBody Map<String, Object> userObj) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return userService.login(userObj);
    }

    @Operation(summary = "유저 정보 API")
    @GetMapping("/info")
    public Response<UserInfoResponse> getUserInfoByUserId(HttpServletRequest request) {
        Response<UserInfoResponse> res = new Response<>();
        if (!jwtProvider.validateToken(request)) {
            res.setError(jwtProvider.returnLoginError());
        } else {
            res = userService.getUserInfoByToken(jwtProvider.getToken(request));
        }

        return res;
    }

    @Operation(summary = "관리자 확인 API")
    @GetMapping("/isAdmin")
    public Response<Boolean> isAdmin(HttpServletRequest request) {
        Response<Boolean> res = new Response<>();
        if (jwtProvider.validateToken(request)) {
            res.setData(jwtProvider.isAdmin(jwtProvider.getToken(request)));
        } else {
            res.setError(jwtProvider.returnLoginError());
        }
        return res;
    }

}
