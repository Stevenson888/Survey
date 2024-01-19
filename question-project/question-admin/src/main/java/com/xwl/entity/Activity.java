package com.xwl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2023-08-03
 */
@Getter
@Setter
@ApiModel(value = "Activity对象", description = "")
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    @ApiModelProperty("活动id")
    private Long activityId;

    @ApiModelProperty("指定类型的用户可参与,对应用户类型")
    private Integer forUserType;

    @ApiModelProperty("活动标题")
    private String title;

    @ApiModelProperty("活动简介")
    private String info;

    @ApiModelProperty("活动规则描述")
    private String content;

    @ApiModelProperty("活动状态：0下线; 1上线")
    private Integer isActive;

//    @ApiModelProperty("答卷数量")
    @TableField(exist = false)
    private Integer answerCount;

    @ApiModelProperty("活动开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty("活动结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    @ApiModelProperty("活动创建人id")
    private Long createUid;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    @ApiModelProperty("修改人id")
    private Long updateUid;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @TableField(exist = false)
    private Paper paper;
    @TableField(exist = false)
    private Project project;

    //覆盖区域
//    @TableField(exist = false)
//    private List<String> activityAreaLimitCityCodeList; //code
    @TableField(exist = false)
    private List<String> areaCodeList;                  //code
    @TableField(exist = false)
    private List<String> areaNameList;                  //name
    @TableField(exist = false)
    private List<ActivityAreaLimit> activityAreaLimitList;  //record - toDB
    @TableField(exist = false)
    private List<List> selectedAreaOptions;               //record - fromPage


    @TableField(exist = false)
    private List<Question> selectQuestionList;                 //选择题
    @TableField(exist = false)
    private List<Question> blankQuestionList;                  //填空题

}
