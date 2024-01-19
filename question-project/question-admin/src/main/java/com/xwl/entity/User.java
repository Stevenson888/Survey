package com.xwl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.awt.print.Book;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 系统用户实体类
 */
@Data  //自动生成get和set方法
//@Setter
//@Getter
@TableName("m_user")
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    //主键 ,注解：指明主键，然后是自动递增
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    @ApiModelProperty("用户ID")
    private Long userId;

    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    @ApiModelProperty("小程序用户ID")
    private Long appUserId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
//    @JsonIgnore
    private String password;

    @ApiModelProperty("实名")
    private String realName;

    @ApiModelProperty("角色标签")
    private String roleFlag;

    @ApiModelProperty("省份编号")
    private String provinceCode;

    @ApiModelProperty("省份名称")
    private String provinceName;

    @ApiModelProperty("城市编号")
    private String cityCode;

    @ApiModelProperty("城市名称")
    private String cityName;

    @ApiModelProperty("区域编号")
    private String districtCode;

    @ApiModelProperty("区域名称")
    private String districtName;

    @ApiModelProperty("性别 0未知 1男 2女")
    private Integer gender;

    @ApiModelProperty("手机号码")
    private String mobile;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("用户状态 1正常")
    private Integer status;

    @ApiModelProperty("创建人id")
    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    private Long createUid;  //createUid是m_user.id, 不是m_user.user_id, m_user.user_id是小程序用户id

    @ApiModelProperty("最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

    @ApiModelProperty("用户建立时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    //一对多查询
    @TableField(exist = false)
    private List<Store> storeList;

    //一对多查询
    @TableField(exist = false)
    private Integer storeNumber;

    //小程序User表字段
    @TableField(exist = false)
    private String appNickname;

    @TableField(exist = false)
    private String confirmPassword;
    @TableField(exist = false)
    private String newPassword;
    @TableField(exist = false)
    private String role;
    @TableField(exist = false)
    private String avatarUrl;
    @TableField(exist = false)
    private String token;
    @TableField(exist = false)
    private List<Menu> menus;

    //小程序User表字段
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime appCreateTime;

}
