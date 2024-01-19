package com.xwl.entity;

import cn.hutool.core.date.DateTime;
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
 * @since 2023-08-12
 */
@Getter
@Setter
@ApiModel(value = "Project对象", description = "")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    @ApiModelProperty("项目id")
    private Long projectId;

    @ApiModelProperty("项目编号")
    private String code;

    @ApiModelProperty("项目名称")
    private String name;

    @ApiModelProperty("项目简介")
    private String info;

    @ApiModelProperty("项目描述")
    private String content;

    @ApiModelProperty("物料种类")
    private String productCategory;

    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    @ApiModelProperty("项目创建人id")
    private Long createMuid;

    @JsonSerialize(using = ToStringSerializer.class)  //可以防止雪花算法生成的ID前后端显示不一致
    @ApiModelProperty("项目修改人id")
    private Long updateMuid;

    @ApiModelProperty("项目开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty("项目结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty("项目创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("项目更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @TableField(exist = false)
    private List<ProjectActivity> allProjectActivityList;
//    @TableField(exist = false)
//    private Integer allProjectActivityListSize;
    @TableField(exist = false)
    private List<Integer> selectedActivityIdList;
    @TableField(exist = false)
    private List<Activity> selectedActivityList;
    @TableField(exist = false)
    private List<ProjectActivity> selectedProjectActivityList;
    @TableField(exist = false)
    private Integer selectedProjectActivityListSize;

//    @TableField(exist = false)
//    private List<Object> selectedActivityList;

    //覆盖区域
    @TableField(exist = false)
    private List<String> activityAreaLimitCityCodeList; //code
    @TableField(exist = false)
    private List<String> areaNameList;                  //name
    @TableField(exist = false)
    private List<ActivityAreaLimit> activityAreaLimitList;  //record

    @TableField(exist = false)
    private List<Files> fileList;

}
