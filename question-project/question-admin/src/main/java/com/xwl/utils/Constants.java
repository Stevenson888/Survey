package com.xwl.utils;

public interface Constants {

    String LOGIN_USER_KEY = "userInfo";

    String CODE_200 = "200"; //成功
    String CODE_401 = "401";  // 权限不足
    String CODE_400 = "400";  // 参数错误
    String CODE_500 = "500"; // 系统错误
    String CODE_600 = "600"; // 用户不存在
    String CODE_601 = "601"; // 密码错误
    String CODE_607 = "607"; // 此用户未激活
    String CODE_602 = "602"; // 请输入用户名密码
    String CODE_603 = "603"; // 此手机号已注册
    String CODE_604 = "604"; // 此用户名已注册
    String CODE_605 = "605"; // 该手机号对应用户未注册小程序，请先注册小程序
    String CODE_606 = "606"; // 门店导入时字段为空


    String CODE_700 = "700"; // licenseNo专卖证号已存在

//    String DICT_TYPE_ICON = "icon";

    String ROLE_DEV = "ROLE_DEV";   // 开发人员
    String ROLE_ADMIN = "ROLE_ADMIN";           // 管理员
    String ROLE_PARTNER = "ROLE_PARTNER";       // 客户经理
    String ROLE_STORE = "ROLE_STORE";           // 零售户
    String ROLE_CUSTOMER = "ROLE_CUSTOMER";     // 消费者


}
