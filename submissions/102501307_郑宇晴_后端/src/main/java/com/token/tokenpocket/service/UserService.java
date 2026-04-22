package com.token.tokenpocket.service;

import com.token.tokenpocket.dto.LoginFormDTO;
import com.token.tokenpocket.pojo.Result;
import jakarta.servlet.http.HttpSession;

public interface UserService {
    Result sendCode(String phone, HttpSession session);

    Result login(LoginFormDTO loginForm, HttpSession session);
}
