package com.example.aigenlease.dto.user.response;

public record UserLoginResponse(
        Long userId,
        String username,
        String token
) {
}