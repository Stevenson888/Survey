package com.xwl.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户登录
 */
@Data
public class UserDTO {
    private Integer id;

    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    private Long userId;

    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    private Long appUserId;

    private String realName;
    private String username;
    private String password;
    private String confirmPassword;
    private String newPassword;
    private String roleFlag;
    private String role;
    private Integer status;

    private String nickname;
    private Integer gender;
    private String mobile;
    private String email;
    private String address;
    private String avatarUrl;

    private String provinceCode;
    private String provinceName;
    private String cityCode;
    private String cityName;
    private String districtCode;
    private String districtName;

    private Date lastLoginTime;
    private Date createTime;
    private Date updateTime;

    private String token;
    private List<Menu> menus;

}

