package com.example.aigenlease.dto.user.response;

import java.time.LocalDateTime;

public record PointsLogResponse(
        int changeAmount,          
        String type,               
        String description,        
        LocalDateTime createdAt
) {
}