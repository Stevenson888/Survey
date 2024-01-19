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
 * @since 2023-08-13
 */
@Getter
@Setter
@TableName("activity_area_limit")
@ApiModel(value = "ActivityAreaLimit对象", description = "")
public class ActivityAreaLimit implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("活动投放城市限制")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("活动id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long activityId;

    @ApiModelProperty("城市编号")
    private String cityCode;

    @ApiModelProperty("黑白名单类型 white白名单，black黑名单")
    private String type;


}
