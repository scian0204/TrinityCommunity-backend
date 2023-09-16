package com.daelim.trinitycommunitybackend.service;

import com.daelim.trinitycommunitybackend.entity.User;
import com.daelim.trinitycommunitybackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserJwtService {
    private final UserRepository userRepository;

    public User getUserByUserId(String userId) {
        return userRepository.getReferenceById(userId);
    }
}
