package com.xwl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

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
 * @since 2023-08-16
 */
@Getter
@Setter
@TableName("activity_paper")
@ApiModel(value = "ActivityPaper对象", description = "")
public class ActivityPaper implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("活动和问卷关联表")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("活动id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long activityId;

    @ApiModelProperty("问卷id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long paperId;

    @ApiModelProperty("问卷总数限制。0为不限")
    private Integer paperLimit;

    @ApiModelProperty("每个门店数量限制，0为不限")
    private Integer paperLimitPerStore;

    @ApiModelProperty("是否删除")
    private Integer isDelete;


}
