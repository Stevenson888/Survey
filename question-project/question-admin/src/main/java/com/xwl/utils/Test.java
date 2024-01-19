package com.xwl.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xwl.entity.AppUser;
import com.xwl.entity.User;
import com.xwl.service.IAppUserService;
import com.xwl.service.IProjectService;
import com.xwl.service.IUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class Test {

    @Resource
    private IUserService userService;
    @Resource
    private IAppUserService appUserService;

    private static String SERIAL_NUMBER = "0001" ;
    private static Test primaryGenerater = null;


    public static void main(String[] args){

//        User returnMUser = new User();
//
//        QueryWrapper<AppUser> appUserQueryWrapper = new QueryWrapper<>();
//        appUserQueryWrapper.lambda().eq(AppUser::getUserType, 8);
//        List<AppUser> appUserList = appUserService.list(appUserQueryWrapper);
//        for (AppUser appUser : appUserList){
//
//            returnMUser.setUserId(SnowflakeIdWorker.getNextId());
//            returnMUser.setPassword("96e79218965eb72c92a549dd5a330112");
//            returnMUser.setCreateUid(new Long(0));
//            returnMUser.setRoleFlag("ROLE_PARTNER");
//
//            returnMUser.setAppUserId(appUser.getUserId());
//            returnMUser.setUsername(StrUtil.isNotEmpty(appUser.getNickname()) ? appUser.getNickname() : appUser.getRealName() );
//            returnMUser.setRealName(appUser.getRealName());
//
//            returnMUser.setProvinceName(appUser.getProvinceName());
//            returnMUser.setProvinceCode(appUser.getProvinceCode());
//            returnMUser.setCityName(appUser.getCityName());
//            returnMUser.setCityCode(appUser.getCityCode());
//            returnMUser.setDistrictName(appUser.getDistrictName());
//            returnMUser.setDistrictCode(appUser.getDistrictCode());
//
//            returnMUser.setGender(appUser.getGender());
//            returnMUser.setMobile(appUser.getMobile());
//            returnMUser.setStatus(appUser.getStatus());
//            returnMUser.setAvatarUrl(appUser.getAvatar());
//            returnMUser.setEmail("");
//            returnMUser.setAddress("");
//
//            returnMUser.setCreateTime(new Date());
//            returnMUser.setUpdateTime(new Date());
//            returnMUser.setLastLoginTime(new Date());
//
//            boolean save = userService.save(returnMUser);

        }






}
