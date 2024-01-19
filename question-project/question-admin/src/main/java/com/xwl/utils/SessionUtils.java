package com.xwl.utils;

import cn.dev33.satoken.stp.StpUtil;
import com.xwl.entity.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionUtils {

    // 获取当前登录的用户信息
    public static User getUser() {
        try {
            System.out.println("===StpUtil.getSession()==="+StpUtil.getSession());
            System.out.println("===StpUtil.getSession()==="+StpUtil.getSession());
            return (User) StpUtil.getSession().get(Constants.LOGIN_USER_KEY);
        } catch (Exception e) {
            log.error("获取用户失败，请登录");
            return null;
        }
    }

}
