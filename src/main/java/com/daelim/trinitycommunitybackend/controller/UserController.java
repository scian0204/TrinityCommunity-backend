package com.daelim.trinitycommunitybackend.controller;

import com.daelim.trinitycommunitybackend.config.CustomAuthorization;
import com.daelim.trinitycommunitybackend.config.JwtProvider;
import com.daelim.trinitycommunitybackend.dto.LoginRequest;
import com.daelim.trinitycommunitybackend.dto.Response;
import com.daelim.trinitycommunitybackend.dto.UserInfoResponse;
import com.daelim.trinitycommunitybackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "유저 API")
public class UserController {
    private final UserService userService;
    private final JwtProvider jwtProvider;


    @Operation(summary = "로그인 여부 확인 API - 로그인 필요")
    @ApiResponse(description = "토큰 검증")
    @CustomAuthorization
    @GetMapping("/isLogin")
    public Response<Boolean> isLogin(HttpServletRequest request) {
        Response<Boolean> res = new Response<>();
        res.setData(true);
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

    @Operation(summary = "유저 정보 API - 로그인 필요")
    @CustomAuthorization
    @GetMapping("/info")
    public Response<UserInfoResponse> getUserInfoByUserId(HttpServletRequest request) {
        return userService.getUserInfoByToken(jwtProvider.getToken(request));
    }

    @Operation(summary = "관리자 확인 API - 로그인 필요")
    @CustomAuthorization(isAdmin = true)
    @GetMapping("/isAdmin")
    public Response<Boolean> isAdmin(HttpServletRequest request) {
        Response<Boolean> res = new Response<>();
        res.setData(true);
        return res;
    }

}
