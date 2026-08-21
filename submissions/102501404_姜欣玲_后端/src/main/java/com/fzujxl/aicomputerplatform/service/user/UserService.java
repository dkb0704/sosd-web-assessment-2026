package com.fzujxl.aicomputerplatform.service.user;

import com.fzujxl.aicomputerplatform.dto.sign.SignResponse;
import com.fzujxl.aicomputerplatform.dto.user.*;

public interface UserService {

    AuthResponse login(LoginRequest loginRequest);

    UserProfileResponse getCurrentUserInfo(Long userId);

    UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request);

    void changePassword(Long userId, ChangePasswordRequest request, String token);

    SignResponse sign(Long userId);

    void logout(String token);


}