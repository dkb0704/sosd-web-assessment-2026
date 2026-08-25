package com.fzujxl.aicomputerplatform.utils;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptPasswordEncoder implements PasswordEncoder {
    private static final int COST = 10;

    @Override
    public String encode(String rawPassword){
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(COST));
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword){
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }

}
