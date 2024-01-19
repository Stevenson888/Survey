package com.xwl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2023-08-05
 */
@Data
@TableName("user")
@ApiModel(value = "User对象", description = "")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户表")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("实名")
    private String realName;

    @ApiModelProperty("头像url")
    private String avatar;

    @ApiModelProperty("性别 0未知 1男 2女")
    private Integer gender;

    @ApiModelProperty("手机号码")
    private String mobile;

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

    @ApiModelProperty("用户类型 1消费者 2 商户 4访问员 8客户经理")
    private Integer userType;

    @ApiModelProperty("用户状态 1正常")
    private Integer status;

    @ApiModelProperty("最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty("用户建立时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty("最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    //零售户,消费者所属门店
    @TableField(exist = false)
    private Store store;

    //管理员,客户经理名下门店 (一对多查询)
    @TableField(exist = false)
    private List<Store> storeList;

    //零售户,消费者所属门店
    @TableField(exist = false)
    private Integer answerAmount;
}
