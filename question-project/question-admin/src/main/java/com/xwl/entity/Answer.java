package com.xwl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2023-07-31
 */
@Getter
@Setter
@ApiModel(value = "Answer对象", description = "")
@ToString
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("答卷总表")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("答卷id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long answerId;

    @ApiModelProperty("活动id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long activityId;

    @ApiModelProperty("问卷id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long paperId;

    @ApiModelProperty("门店id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long storeId;

    @ApiModelProperty("客户经理用户id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long partnerUid;

    @ApiModelProperty("答题人用户id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty("答题开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;

    @ApiModelProperty("答题结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty("答题用时多少秒")
    private Integer duration;

    @ApiModelProperty("答卷总分值")
    private Integer totalScore;

    @ApiModelProperty("经度")
    private BigDecimal longitude;

    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    @ApiModelProperty("经纬度定位")
    private String location;

    @ApiModelProperty("答卷不准确？1是(不准确) 0否")
    private Integer isIncorrect;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private AppUser appUser;
    @TableField(exist = false)
    private Store store;
    @TableField(exist = false)
    private Activity activity;
    @TableField(exist = false)
    private Integer total;
    @TableField(exist = false)
    private String storeName;

    //AnswerMapper: getAnswersByActivityIdWithParm
    @TableField(exist = false)
    private String title;           //activity.title
    @TableField(exist = false)
    private String licenseNo;       //store.licenseNo
    @TableField(exist = false)
    private String name;            //store.name
    @TableField(exist = false)
    private String nickname;       //user.nickname
    @TableField(exist = false)
    private String mobile;          //user.mobile
     @TableField(exist = false)
    private String userType;          //user.user_type


}
