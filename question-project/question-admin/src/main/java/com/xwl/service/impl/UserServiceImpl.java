package com.xwl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwl.entity.*;
import com.xwl.exception.ServiceException;
import com.xwl.mapper.UserMapper;
import com.xwl.service.*;
import com.xwl.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private IAppUserService appUserService;
    @Resource
    private IUserService userService;

    @Override
    public User register(User user) {
        User insertUser = getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if (insertUser == null) {
            insertUser = new User();
            BeanUtil.copyProperties(user, insertUser, true);

            //TODO: 多了一位
            Long userID = SnowflakeIdWorker.getNextId();
            insertUser.setUserId(userID);
            insertUser.setAppUserId(new Long(0));

            insertUser.setPassword(SecureUtil.md5(user.getPassword()));

            //TODO:
            insertUser.setCreateUid(userID);

            insertUser.setStatus(1);
            insertUser.setCreateTime(new Date());
            insertUser.setUpdateTime(new Date());
            insertUser.setLastLoginTime(new Date());
//        one.setAvatar("888");

            save(insertUser);  // 把 copy完之后的用户对象存储到数据库
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名已存在");
        }
        return insertUser;
    }

    @Override
    public int updatePassword(User user) {
        int update = userMapper.updatePassword(user);
        if (update < 1) {
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }
        return update;
    }

    @Override
    public int updateOthersPassword(User user) {
        int update = userMapper.updateOthersPassword(user);
        if (update < 1) {
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }
        return update;
    }

    @Override
    public int updateUserStatus(User user) {
        int update = userMapper.updateUserStatus(user);
        return update;
    }

//    @Override
//    public List<User> getStoresByUserId(Long userId , String name) {
//        // 一对多查询
//        return this.baseMapper.getStoresByUserId(userId, name);
//        //        query.lambda().like(SysUser::getUsername,userParm.getUsername());
//    }


//    //通过userId，查找小程序User表的记录信息
//    public AppUser getUserFromApp(Long userId){
//        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(AppUser::getUserId, userId);
//        AppUser appUser = appUserService.getOne(queryWrapper);
//        return appUser;
//    }

    //在m_user表中查询：通过后台用户id m_user.userId，获取小程序用户id m_user.app_user_id
    public Long getAppUserIdFromMUserId(Long mUserId){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUserId, mUserId);
        User user = userService.getOne(queryWrapper);
        if( user == null ){
            return new Long(0);
        }
        Long appUserId = user.getAppUserId();
        return appUserId;
    }
    //在m_user表中查询：通过小程序用户id m_user.app_user_id，获取后台用户id m_user.userId
    public Long getMUserIdFromAppUserId(Long appUserId){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getAppUserId, appUserId);
        User user = userService.getOne(queryWrapper);
        if( user == null ){
            return new Long(0);
        }
        Long mUserId = user.getUserId();
        return mUserId;
    }


}
