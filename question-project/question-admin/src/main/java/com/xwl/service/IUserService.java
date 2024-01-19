package com.xwl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xwl.entity.*;

public interface IUserService extends IService<User>{

    User register(User user);

    int updatePassword(User user);
    int updateOthersPassword(User user);
    int updateUserStatus(User user);

    //MUser表内：
    Long getAppUserIdFromMUserId(Long userId);
    //MUser表内：
    Long getMUserIdFromAppUserId(Long userId);

        // 一对多查询
//    List<User> getStoresByUserId(Long userId, String name);

}
