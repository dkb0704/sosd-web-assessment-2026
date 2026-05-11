package com.example.aigenlease.dto.user.response;

import java.time.LocalDate;

public record CheckInStatusResponse(
        boolean checkedInToday,
        LocalDate lastCheckInDate,
        int consecutiveDays
) {
}