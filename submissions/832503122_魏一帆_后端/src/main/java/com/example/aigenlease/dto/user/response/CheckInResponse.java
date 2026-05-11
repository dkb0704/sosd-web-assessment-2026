package com.example.aigenlease.dto.user.response;

public record CheckInResponse(
        boolean success,
        int todayPoints,
        int totalPoints
) {
}