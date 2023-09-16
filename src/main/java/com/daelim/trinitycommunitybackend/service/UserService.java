package com.daelim.trinitycommunitybackend.service;

import com.daelim.trinitycommunitybackend.config.JwtProvider;
import com.daelim.trinitycommunitybackend.dto.Error;
import com.daelim.trinitycommunitybackend.dto.Response;
import com.daelim.trinitycommunitybackend.dto.UserInfoResponse;
import com.daelim.trinitycommunitybackend.entity.User;
import com.daelim.trinitycommunitybackend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    ObjectMapper objMpr = new ObjectMapper();
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public Response<String> login(Map<String, Object> userObj) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String userId = (String) userObj.get("userId");
        String password = (String) userObj.get("password");
        Response<String> res = new Response<>();

        Optional<User> byUserId = userRepository.findByUserId(userId);
        if (byUserId.isPresent()) {
            User user = byUserId.get();
            if (user.getPassword().equals(encrypt(password))) {
                String token = jwtProvider.createToken(userId);
                res.setData(token);
                return res;
            } else {
                Error error = new Error();
                error.setErrorId(1);
                error.setMessage("userId는 존재하지만 password가 일치하지 않음");
                res.setError(error);
                return res;
            }
        } else {
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("userId 존재하지 않음");
            res.setError(error);
            return res;
        }
    }

    public Response<UserInfoResponse> getUserInfoByToken(String token) {
        Response<UserInfoResponse> res = new Response<>();
        String userId = jwtProvider.getUserId(token);
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            res.setData(new UserInfoResponse(userOptional.get()));
        } else {
            Error error = new Error();
            error.setErrorId(1);
            error.setMessage("해당 아이디를 가진 유저가 없음");
            res.setError(error);
        }
        return res;
    }



    private String encrypt(String pw) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(pw.getBytes("utf-8"));
        return bytesToHex(md.digest());
    }
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b: bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
