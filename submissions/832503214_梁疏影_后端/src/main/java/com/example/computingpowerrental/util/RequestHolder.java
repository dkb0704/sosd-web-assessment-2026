package com.example.computingpowerrental.util;

import com.example.computingpowerrental.entity.User;

/**
 * @author Lark
 * @ date 2026/5/24  18:20
 * @ description 请求上下文持有类，使用ThreadLocal保存当前请求的用户信息
 */
public class RequestHolder {
    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<Integer> roleHolder = new ThreadLocal<>();
    private static final ThreadLocal<User> userHolder = new ThreadLocal<>();

    //设置完整的用户信息
    public static void setUser(User user) {
        userHolder.set(user);
        if (user != null) {
            userIdHolder.set(user.getId());
            roleHolder.set(user.getRole());
        }else{
            remove();
        }
    }

    //仅设置用户ID和角色（从Token解析时使用）
    public static void setUserId(Long userId) {
        userIdHolder.set(userId);
    }

    //设置用户角色
    public static void setRole(Integer role) {
        roleHolder.set(role);
    }

    //同时设置用户 ID 和角色
    public static void setUserIdAndRole(Long userId, Integer role) {
        userIdHolder.set(userId);
        roleHolder.set(role);
    }

    //获取完整的用户信息
    public static User getUser() {
        return userHolder.get();
    }

    //获取用户ID
    public static Long getUserId() {
        Long userId = userIdHolder.get();
        if (userId != null) {
            return userId;
        }
        User user = userHolder.get();
        return user != null ? user.getId() : null;
    }

    //获取用户角色
    public static Integer getRole() {
        Integer role = roleHolder.get();
        if (role != null) {
            return role;
        }
        User user = userHolder.get();
        return user != null ? user.getRole() : null;
    }

    //判断当前用户是否为管理员
    public static boolean isAdmin() {
        Integer role = getRole();
        return role != null && role == 1;
    }

    //清理ThreadLocal中的数据（必须在请求结束后调用）
    public static void remove() {
        userHolder.remove();
        roleHolder.remove();
        userIdHolder.remove();
    }
}
