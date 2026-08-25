package com.fzujxl.aicomputerplatform.service;

import com.fzujxl.aicomputerplatform.dto.AuthResponse;
import com.fzujxl.aicomputerplatform.dto.LoginRequest;
import com.fzujxl.aicomputerplatform.dto.RegisterRequest;

public interface UserService {
    AuthResponse register(RegisterRequest registerRequest);

    AuthResponse login(LoginRequest loginRequest);
}
